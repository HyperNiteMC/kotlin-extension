package com.hypernite.mc.kotlinex.dsl.inventory

import com.hypernite.mc.hnmc.core.builders.InventoryBuilder
import com.hypernite.mc.hnmc.core.managers.builder.AbstractInventoryBuilder
import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import org.bukkit.inventory.Inventory

class InventoryDSLBuilder(title: String, row: Int) : Buildable<Inventory>{

    private val builder: AbstractInventoryBuilder = InventoryBuilder(row, title)

    infix fun Int.row(slot: Int): Int{
        return (this - 1) * 9 + slot
    }

    override fun build(): Inventory {
        throw Exception("trying to run API.jar into server")
    }


}