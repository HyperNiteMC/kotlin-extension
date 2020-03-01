package com.hypernite.mc.kotlinex

import kotlin.reflect.KClass

interface ArgumentParser {

    fun <T : Any> registerParser(cls: KClass<T>, index: Int, parse: (List<String>) -> T?)

    fun <T : Any> tryParse(cls: KClass<T>, args: MutableList<String>): T?

}