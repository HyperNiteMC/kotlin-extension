package com.hypernite.mc.kotlinex.dsl.inventory

import com.hypernite.mc.hnmc.core.builders.InventoryBuilder
import com.hypernite.mc.hnmc.core.managers.builder.AbstractInventoryBuilder
import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryDSLBuilder(private val row: Int) : Buildable<Inventory> {

    var title: String = ""
    val ONE_ROW = AbstractInventoryBuilder.ONE_ROW
    val CENTER = AbstractInventoryBuilder.CENTER
    val START = AbstractInventoryBuilder.START
    val END = AbstractInventoryBuilder.END

    private val builder: AbstractInventoryBuilder by lazy {
        InventoryBuilder(row, title)
    }

    infix fun Int.row(slot: Int): Int {
        return (this - 1) * 9 + slot
    }

    fun slot(slot: Int, item: () -> ItemStack) {
        builder.item(slot, item())
    }

    fun slot(vararg slots: Int, item: () -> ItemStack) {
        for (slot in slots) {
            slot(slot, item)
        }
    }

    fun slot(row: Int, slot: Int, item: () -> ItemStack) {
        builder.item(row, slot, item())
    }

    val Int.single: IntRange
        get() = this..this

    fun fill(row: IntRange, column: IntRange, item: () -> ItemStack) {
        val stack = item()
        for (i in column) {
            for (r in row) {
                builder.item(r, i, stack)
            }
        }
    }

    fun fillRow(row: IntRange, item: () -> ItemStack) {
        val stack = item()
        for (r in row) {
            builder.fillRow(r, stack)
        }
    }

    fun fillColumn(column: IntRange, item: () -> ItemStack) {
        val stack = item()
        for (i in column) {
            for (r in 1..6) {
                builder.item(r, i, stack)
            }
        }
    }

    fun fill(item: () -> ItemStack) {
        val stack = item()
        val slots = this.row * 9 - 1
        for (s in 0 until slots) {
            builder.item(s, stack)
        }
    }

    fun center(item: () -> ItemStack) {
        builder.center(item())
    }

    fun ring(item: () -> ItemStack) {
        builder.ring(item())
    }


    operator fun ItemStack.unaryPlus() {
        builder.item(this)
    }

    override fun build(): Inventory {
        return builder.build()
    }


}