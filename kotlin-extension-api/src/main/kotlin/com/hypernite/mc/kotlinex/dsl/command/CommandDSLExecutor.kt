package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.kotlinex.tryParse
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class CommandDSLExecutor(
        val sender: CommandSender,
        arg: Map<String, KClass<*>>,
        properties: Map<String, PlaceholderDSLBuilder.Property>,
        args: Iterator<String> = listOf<String>().iterator()) {

    private val parsed: Map<String, Any?>
    val args: List<String>

    init {
        this.parsed = mutableMapOf()
        this.args = mutableListOf()
        for ((label, cls) in arg) {
            val optional = properties[label]?.optional ?: true
            if (!args.hasNext() && optional) continue
            try {
                parsed += label to cls.tryParse(args, sender)
            } catch (e: NoSuchElementException) {
                val msg = "有轉換器獲取了多餘的參數。($cls)"
                throw CommandArgs.ArgumentParseException(msg)
            }
        }
        while (args.hasNext()) {
            this.args += args.next()
        }
    }

    val List<String>.asArray: Array<String>
        get() = args.toTypedArray()

    val List<String>.joinToOne: String
        get() = args.joinToString(" ")

    inline fun <reified T : Any> List<String>.getAs(fromLast: Boolean = false): T? {
        val toParse = LinkedList(this)
        return T::class.tryParse(toParse.iterator(), sender)
    }

    operator fun <T : Any> get(label: String): T {
        return parsed[label] as T
    }

    fun <T : Any> getSafe(label: String): T? {
        return parsed[label] as? T
    }


}