package com.hypernite.mc.kotlinex.dsl.itemstack

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import org.bukkit.enchantments.Enchantment

class EnchantDSLBuilder : Buildable<Map<Enchantment, Int>>{
    private val map: MutableMap<Enchantment, Int>  = mutableMapOf()

    infix fun Enchantment.level(level: Int){
        map[this] = level
    }

    override fun build(): Map<Enchantment, Int> {
        return map
    }


}