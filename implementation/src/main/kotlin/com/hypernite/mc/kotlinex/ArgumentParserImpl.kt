package com.hypernite.mc.kotlinex

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object ArgumentParserImpl : ArgumentParser {

    private val map: MutableMap<KClass<out Any>, (List<String>) -> Any?> = ConcurrentHashMap()
    private val consume: MutableMap<KClass<out Any>, Int> = ConcurrentHashMap()

    override fun <T : Any> registerParser(cls: KClass<T>, index: Int, parse: (List<String>) -> T?) {
        map[cls] = parse
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> tryParse(cls: KClass<T>, args: MutableList<String>): T? {
        val parse = map[cls] ?: throw IllegalStateException("尚未註冊 $cls 的轉換器")
        val o = parse(args)
        for (i in 0 until (consume[cls] ?: 0)) {
            args.removeAt(i)
        }
        return o as? T
    }
}