package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import kotlin.reflect.KClass

class PlaceholderDSLBuilder : Buildable<Map<String, KClass<*>>> {

    private val map: MutableMap<String, KClass<out Any>> = mutableMapOf()

    inline fun <reified T : Any> cast(label: String): Pair<String, KClass<out Any>>{
        return label to T::class
    }

    operator fun Pair<String, KClass<out Any>>.unaryMinus(){
        map[this.first] = this.second
    }

    override fun build(): Map<String, KClass<*>> {
        return map
    }

}