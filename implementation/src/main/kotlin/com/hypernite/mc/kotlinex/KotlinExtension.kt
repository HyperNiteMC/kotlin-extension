package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.main.HyperNiteMC
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
        this.registerParser()
        logger.info("Default argument parser registered")
        logger.info("KotlinExtension enabled.")
    }

    private fun registerParser() {
        registerParsing(arrayOf("integer")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toInt()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的數字")
            }
        }
        registerParsing(arrayOf("double")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toDouble()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的數字")
            }
        }
        registerParsing(arrayOf("float")) { args, _ ->
            val ctx = args.next()
            try {
                ctx.toFloat()
            } catch (e: NumberFormatException) {
                args.throwError("$ctx 並不是有效的浮點數")
            }
        }
        registerParsing(arrayOf("bool")) { args, _ -> args.next().toBoolean() }
        registerParsing(arrayOf("player")) { args, _ ->
            val ctx = args.next()
            Bukkit.getPlayer(ctx) ?: args.throwError("找不到線上玩家 $ctx")
        }
        registerParsing(arrayOf("player")) { args, sender ->
            val ctx = args.next()
            val p = Bukkit.getPlayer(ctx) ?: sender as? Player
            val pp = p ?: args.throwError("you are not player and cannot find player $ctx")
            PlayerOrSource(pp)
        }
        registerParsing(arrayOf("player")) { args, _ ->
            val ctx = args.next()
            Bukkit.getPlayerUniqueId(ctx)?.let { Bukkit.getOfflinePlayer(it) } ?: args.throwError("找不到玩家 $ctx")
        }
        registerParsing(arrayOf("player")) { args, sender ->
            val ctx = args.next()
            val p = Bukkit.getPlayerUniqueId(ctx)?.let { Bukkit.getOfflinePlayer(it) } ?: sender as? OfflinePlayer
            val pp = p ?: args.throwError("you are not player and cannot find player $ctx")
            UserOrSource(pp)
        }
        registerParsing(arrayOf("world")) { args, _ ->
            val ctx = args.next()
            Bukkit.getWorld(ctx) ?: args.throwError("找不到世界 $ctx")
        }
        registerParsing(arrayOf("environment")) { args, _ ->
            val ctx = args.next().toUpperCase()
            try {
                World.Environment.valueOf(ctx)
            } catch (e: IllegalArgumentException) {
                args.throwError("找不到世界類型 $ctx, 可用世界類型: ${World.Environment.values().joinString(", ")}")
            }
        }
        registerParsing(arrayOf("world", "x", "y", "z")) { args, sender ->
            val world = World::class.tryParse(args, sender)
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            Location(world, x, y, z)
        }
        registerParsing(arrayOf("x", "y", "z")) { args, sender ->
            val player = sender as? Player ?: args.throwError("you are not player")
            val world = player.world
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            LocationSelf(world, x, y, z)
        }
        registerParsing(arrayOf("x", "y", "z")) { args, sender ->
            val x = Double::class.tryParse(args, sender)
            val y = Double::class.tryParse(args, sender)
            val z = Double::class.tryParse(args, sender)
            Vector(x, y, z)
        }
        registerParsing(arrayOf("message")) { args, _ ->
            val list = mutableListOf<String>()
            while (args.hasNext()) {
                list += args.next()
            }
            list
        }
        registerParsing(arrayOf("string")) { args, _ -> args.next() }
    }


}