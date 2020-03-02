package com.hypernite.mc.kotlinex.dsl

import com.hypernite.mc.hnmc.core.misc.commands.CommandNode
import com.hypernite.mc.kotlinex.dsl.command.CommandDSLBuilder
import com.hypernite.mc.kotlinex.dsl.inventory.InventoryDSLBuilder
import com.hypernite.mc.kotlinex.dsl.itemstack.ItemDSLBuilder
import com.hypernite.mc.kotlinex.dsl.listener.DSLEventListener
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

fun item(material: Material, setup: ItemDSLBuilder.() -> Unit): ItemStack {
    val builder = ItemDSLBuilder(material)
    builder.setup()
    return builder.build()
}

fun listener(plugin: Plugin, setup: DSLEventListener.() -> Unit) {
    val kotlinListener = DSLEventListener(plugin)
    kotlinListener.setup()
}

fun inventory(row: Int = 6, setup: InventoryDSLBuilder.() -> Unit): Inventory {
    val inventoryBuilder = InventoryDSLBuilder(row)
    inventoryBuilder.setup()
    return inventoryBuilder.build()
}

fun command(name: String, setup: CommandDSLBuilder.() -> Unit): CommandNode {
    val builder = CommandDSLBuilder(name)
    builder.setup()
    return builder.build()
}