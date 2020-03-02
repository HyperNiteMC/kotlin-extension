package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.main.HyperNiteMC
import com.hypernite.mc.kotlinex.dsl.command.element.Literal
import com.hypernite.mc.kotlinex.dsl.command.element.LocationSelf
import com.hypernite.mc.kotlinex.dsl.command.element.PlayerOrSource
import com.hypernite.mc.kotlinex.dsl.command.element.UserOrSource
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class KotlinExtension : JavaPlugin() {

    companion object {
        internal lateinit var plugin: Plugin
    }

    override fun onEnable() {
        plugin = this
        val dataSource = HyperNiteMC.getAPI().sqlDataSource
        KCore.setupMySQL(dataSource)
        logger.info("MySQL for kotlin has been setup completed.")
        logger.info("Registering default argument parser...")
        this.registerParser(KCore.argumentParser)
        logger.info("Default argument parser registered")
        logger.info("KotlinExtension enabled.")
    }

    private fun registerParser(parser: ArgumentParser) {
        parser.registerParser(Int::class, arrayOf("integer")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toInt()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的數字")
            }
        }
        parser.registerParser(Double::class, arrayOf("double")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toDouble()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的數字")
            }
        }
        parser.registerParser(Float::class, arrayOf("float")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toFloat()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的浮點數")
            }
        }
        parser.registerParser(Boolean::class, arrayOf("bool")) { args, _ -> args.next().toBoolean() }
        parser.registerParser(Player::class, arrayOf("player")) { args, _ ->
            val ctx = args.next()
            Bukkit.getPlayer(ctx) ?: args.throwError("找不到線上玩家 $ctx")
        }
        parser.registerParser(PlayerOrSource::class, arrayOf("player")) { args, sender ->
            val ctx = args.next()
            val p = Bukkit.getPlayer(ctx) ?: sender as? Player
            val pp = p ?: args.throwError("you are not player and cannot find player $ctx")
            PlayerOrSource(pp)
        }
        parser.registerParser(OfflinePlayer::class, arrayOf("player")) { args, _ ->
            val ctx = args.next()
            Bukkit.getPlayerUniqueId(ctx)?.let { Bukkit.getOfflinePlayer(it) } ?: args.throwError("找不到玩家 $ctx")
        }
        parser.registerParser(UserOrSource::class, arrayOf("player")) { args, sender ->
            val ctx = args.next()
            val p = Bukkit.getPlayerUniqueId(ctx)?.let { Bukkit.getOfflinePlayer(it) } ?: sender as? OfflinePlayer
            val pp = p ?: args.throwError("you are not player and cannot find player $ctx")
            UserOrSource(pp)
        }
        parser.registerParser(World::class, arrayOf("world")) { args, _ ->
            val ctx = args.next()
            Bukkit.getWorld(ctx) ?: args.throwError("找不到世界 $ctx")
        }
        parser.registerParser(World.Environment::class, arrayOf("environment")) { args, _ ->
            val ctx = args.next().toUpperCase()
            try {
                World.Environment.valueOf(ctx)
            } catch (e: IllegalArgumentException) {
                args.throwError("找不到世界類型 $ctx, 可用世界類型: ${World.Environment.values().joinString(", ")}")
            }
        }
        parser.registerParser(Location::class, arrayOf("world", "x", "y", "z")) { args, sender ->
            val world = World::class.tryParse(args, sender)
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            Location(world, x, y, z)
        }
        parser.registerParser(LocationSelf::class, arrayOf("x", "y", "z")) { args, sender ->
            val player = sender as? Player ?: args.throwError("you are not player")
            val world = player.world
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            LocationSelf(world, x, y, z)
        }
        parser.registerParser(Vector::class, arrayOf("x", "y", "z")) { args, sender ->
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            Vector(x, y, z)
        }
        parser.registerParser(Literal::class, arrayOf("message")) { args, _ ->
            val list = mutableListOf<String>()
            while (args.hasNext()) {
                list += args.next()
            }
            Literal(list)
        }
        parser.registerParser(String::class, arrayOf("string")) { args, _ -> args.next() }
    }


}