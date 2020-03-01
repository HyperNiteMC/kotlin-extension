package com.hypernite.mc.kotlinex.dsl.listener

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

class DSLEventListener(val plugin: Plugin) : Listener {

    inline fun <reified T : Event> listen(priority: EventPriority = EventPriority.NORMAL, crossinline handle: T.() -> Unit) {
        plugin.server.pluginManager.registerEvent(T::class.java, this, priority, { _, event ->
            handle.invoke(event as T)
        }, plugin)
    }

}