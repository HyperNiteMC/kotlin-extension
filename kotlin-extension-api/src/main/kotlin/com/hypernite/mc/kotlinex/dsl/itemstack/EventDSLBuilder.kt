package com.hypernite.mc.kotlinex.dsl.itemstack

import com.hypernite.mc.hnmc.core.managers.builder.AbstractItemStackBuilder
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent

class EventDSLBuilder(private val builder: AbstractItemStackBuilder) {

    fun click(setup: (InventoryClickEvent) -> Unit){
        builder.onClick(setup)
    }

    fun interact(setup: (PlayerInteractEvent) -> Unit){
        builder.onInteract(setup)
    }

}