package com.hypernite.mc.kotlinex.dsl.itemstack

import com.hypernite.mc.hnmc.core.builders.ItemStackBuilder
import com.hypernite.mc.hnmc.core.managers.builder.AbstractItemStackBuilder
import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import com.hypernite.mc.kotlinex.dsl.ListDSLBuilder
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemDSLBuilder(material: Material) : Buildable<ItemStack> {

    private val builder: AbstractItemStackBuilder = ItemStackBuilder(material)
    private val eventDSLBuilder: EventDSLBuilder = EventDSLBuilder(builder)

    fun name(name: String) {
        builder.displayName(name)
    }

    fun amount(amount: Int) {
        builder.stack(amount)
    }

    fun damage(durability: Int) {
        builder.durability(durability)
    }

    fun unbreakable(unbreakable: Boolean) {
        builder.unbreakable(unbreakable)
    }

    fun headSkin(uid: UUID, name: String = ""){
        if (name.isBlank()){
            builder.head(uid)
        }else{
            builder.head(uid, name)
        }
    }

    fun modelData(data: Int) {
        builder.modelData(data)
    }

    fun event(setup: EventDSLBuilder.() -> Unit) {
        eventDSLBuilder.setup()
    }

    fun lore(vararg lore: String){
        builder.lore(*lore)
    }

    fun lore(setup: ListDSLBuilder<String>.() -> Unit = {}) {
        val loreBuilder = ListDSLBuilder<String>()
        loreBuilder.setup()
        builder.lore(loreBuilder.build())
    }

    fun itemFlag(setup: ListDSLBuilder<ItemFlag>.() -> Unit = {}){
        val flagBuilder = ListDSLBuilder<ItemFlag>()
        flagBuilder.setup()
        builder.itemFlags(*flagBuilder.build().toTypedArray())
    }

    fun itemFlag(vararg flags: ItemFlag){
        builder.itemFlags(*flags)
    }

    fun openUI(supplier: () -> Inventory){
        builder.openGui(supplier)
    }

    fun enchant(map: Map<Enchantment, Int>){
        builder.enchant(map)
    }

    fun enchant(setup: EnchantDSLBuilder.() -> Unit = {}) {
        val enchantBuilder = EnchantDSLBuilder()
        enchantBuilder.setup()
        builder.enchant(enchantBuilder.build())
    }

    override fun build(): ItemStack {
        return builder.build()
    }
}