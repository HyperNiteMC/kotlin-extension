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

    fun joined(label: String): CommandElement {
        return CommandElement(label, Joined::class)
    }

    operator fun CommandElement.unaryMinus() {
        map[this.label] = this.type
        property[this.label] ?: Property().also { property[this.label] = it }
    }

    fun CommandElement.optional(): CommandElement {
        val p = property[this.label] ?: Property().also { property[this.label] = it }
        p.optional = true
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
            (if (it.value == Joined::class) arrayOf(it.key) else KCore.argumentParser.getPlaceholders(it.value)).map { s ->
                val optional = property[it.key]?.optional ?: false
                if (optional) {
                    if (s == it.key) "[$s]" else "[$s:${it.key}]"
                } else {
                    if (s == it.key) "<$s>" else "<$s:${it.key}>"
                }
            }.toList()
        }


    internal sealed class Joined

    data class Property(
            var optional: Boolean = false
    )

    data class CommandElement(
            internal val label: String,
            internal val type: KClass<out Any>
    )

}