package com.hypernite.mc.kotlinex

import com.hypernite.mc.kotlinex.dsl.command.CommandArgs
import org.bukkit.command.CommandSender
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

object ArgumentParserImpl : ArgumentParser {

    private val map: MutableMap<KClass<out Any>, Pair<Array<String>, (CommandArgs, CommandSender) -> Any?>> = ConcurrentHashMap()

    override fun <T : Any> registerParser(cls: KClass<T>, tags: Array<String>, parse: (CommandArgs, CommandSender) -> T) {
        map[cls] = tags to parse
        KotlinExtension.plugin.logger.info("Registered Parser for $cls")
    }

    override fun <T : Any> tryParse(cls: KClass<T>, args: Iterator<String>, sender: CommandSender): T {
        val carg = CommandArgs(listOf<String>().iterator())
        val pair = map[cls] ?: let {
            if (cls.java.isEnum) {
                getPlaceholders(cls) to null
            } else {
                carg.throwError("尚未註冊 $cls 的轉換器")
            }
        }
        val parse = pair.second
        val arg = mutableListOf<String>()
        for (i in pair.first.indices) {
            if (!args.hasNext()) carg.throwError("缺少參數: ${getPlaceholders(cls).drop(arg.size).joinToString { "<$it>" }}")
            arg += args.next()
        }
        val commandArg = CommandArgs(arg.iterator())
        val o = parse?.invoke(commandArg, sender) ?: enumParse(cls, commandArg)
        return cls.safeCast(o) ?: carg.throwError("形態轉換失敗或參數為空值。")
    }

    private fun enumParse(cls: KClass<out Any>, args: CommandArgs): Any? {
        val ctx = args.next().toUpperCase()
        return cls.java.enumConstants.find { c -> (c as Enum<*>).name == ctx }
                ?: let{
                    val cs = cls.java.enumConstants
                    val variables = if (cs.size > 20) {
                        cs.take(20).joinToString(", ", postfix = "...")
                    } else {
                        cs.joinToString(", ")
                    }
                    args.throwError("未知的變數，可用變數: $variables")
                }
    }

    override fun getPlaceholders(cls: KClass<out Any>): Array<String> {
        return map[cls]?.first ?: let {
            if (cls.java.isEnum) {
                arrayOf(cls.java.simpleName)
            } else {
                throw IllegalStateException("尚未註冊 $cls 的轉換器")
            }
        }
    }
}