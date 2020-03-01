package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory
import kotlin.reflect.KClass

val ConfigFactory.forKotlin: ConfigFactory
    get() = KCore.forKotlin(this)

fun <T : Any> KClass<T>.tryParse(args: MutableList<String>): T?{
    return KCore.argumentParser.tryParse(this, args)
}
