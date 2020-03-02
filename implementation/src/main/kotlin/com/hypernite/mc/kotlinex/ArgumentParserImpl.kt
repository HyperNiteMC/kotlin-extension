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
        val carg = CommandArgs(args)
        val pair = map[cls] ?: throw carg.throwError("尚未註冊 $cls 的轉換器")
        val parse = pair.second
        val arg = mutableListOf<String>()
        for (i in pair.first.indices) {
            if (!args.hasNext()) throw carg.throwError("參數過少。")
            arg += args.next()
        }
        val o = parse(CommandArgs(arg.iterator()), sender)
        return cls.safeCast(o) ?: throw carg.throwError("形態轉換失敗。")
    }

    override fun getPlaceholders(cls: KClass<out Any>): Array<String> {
        return map[cls]?.first ?: throw IllegalStateException("尚未註冊 $cls 的轉換器")
    }
}