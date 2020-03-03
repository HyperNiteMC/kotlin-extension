import com.hypernite.mc.hnmc.core.main.HyperNiteMC
import com.hypernite.mc.kotlinex.KCore
import com.hypernite.mc.kotlinex.dsl.command
import com.hypernite.mc.kotlinex.dsl.item
import com.hypernite.mc.kotlinex.dsl.listener
import com.hypernite.mc.kotlinex.forKotlin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin


lateinit var plugin: JavaPlugin
fun main() {
    val factory = HyperNiteMC.getAPI().factory.getConfigFactory(plugin).forKotlin
    KCore.argumentParser.registerParser(Player::class, arrayOf("name")) { list, _ ->
        Bukkit.getPlayer(list.next()) ?: list.throwError("player not online")
    }
    KCore.argumentParser.registerParser(Location::class, arrayOf("x", "y", "z")) { args, sender ->
        val player = sender as? Player ?: args.throwError("error")
        val world = player.world
        try {
            val x = args.next().toDouble()
            val y = args.next().toDouble()
            val z = args.next().toDouble()
            Location(world, x, y, z)
        } catch (e: NumberFormatException) {
            args.throwError("not a valid number!, ${e.message}")
        }
    }
}

fun registerListener(){

    listener(plugin = plugin){

        listen<PlayerJoinEvent> {
            player.sendMessage("you joined!!")
        }

        listen<PlayerQuitEvent> {
            player.sendMessage("you quited!")
        }

    }
}

fun commandRegister(){
    command("test"){
        permission("test.use")
        description("test command")
        alias {
            -"tester"
            -"testing"
        }
        placeholder {
            -cast<Player>("player")
            -cast<Location>("loc").optional()
        }

        tabComplete { commandSender, list ->
            -"abc"
            -commandSender.name
            for (player in Bukkit.getOnlinePlayers()) {
                -player.name
            }
            for (s in list) {
                -s
            }
        }

        execute {
            val player: Player = this["player"]
            val loc: Location? = getSafe("location")
            sender.sendMessage("your player is ${player.displayName}")
            player.sendMessage("your location world is ${loc?.world?.name ?: "null"}")
            true
        }
    }
}

fun itemBuild(){
    item(material = Material.STONE) {

        name("my item")
        amount(11)
        damage(20)
        unbreakable(true)

        lore {
            - "line one"
            - "line two"
            - "line three"
        }

        itemFlag{
            - ItemFlag.HIDE_ATTRIBUTES
            - ItemFlag.HIDE_UNBREAKABLE
        }

        enchant {
            Enchantment.ARROW_DAMAGE level 2
            Enchantment.CHANNELING level 1
            Enchantment.DIG_SPEED level 3
        }

        event {
            click {
                it.whoClicked.sendMessage("you ${it.click} clicked the item!!")
            }

            interact {
                it.player.sendMessage("your action: ${it.action}")
            }
        }
    }
}
