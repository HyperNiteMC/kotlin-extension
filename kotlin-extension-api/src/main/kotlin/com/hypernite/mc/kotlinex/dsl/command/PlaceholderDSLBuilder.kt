package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import com.hypernite.mc.kotlinex.KCore
import kotlin.reflect.KClass

class PlaceholderDSLBuilder : Buildable<Map<String, KClass<*>>> {

    private val map: MutableMap<String, KClass<out Any>> = mutableMapOf()
    private val property: MutableMap<String, Property> = mutableMapOf()

    inline fun <reified T : Any> cast(label: String): CommandElement {
        return CommandElement(label, T::class)
    }

    operator fun CommandElement.unaryMinus() {
        map[this.label] = this.type
        property[this.label] = Property()
    }

    fun CommandElement.optional(): CommandElement {
        property[this.label]?.optional = true
        return this
    }

    override fun build(): Map<String, KClass<*>> {
        return map
    }

    fun property(): Map<String, Property> {
        return property
    }

    val toPlaceholders: List<String>
        get() = map.entries.flatMap {
            KCore.argumentParser.getPlaceholders(it.value).map { s ->
                val p = property[it.key] ?: return@map s
                return@map if (p.optional) "[$s]" else "<$s>"
            }.toList()
        }


    data class Property(
            var optional: Boolean = false
    )

    data class CommandElement(
            internal val label: String,
            internal val type: KClass<out Any>
    )

}