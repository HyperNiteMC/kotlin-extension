package com.hypernite.mc.kotlinex.dsl

import com.hypernite.mc.hnmc.core.managers.builder.Buildable

class ListDSLBuilder<T> : Buildable<List<T>> {

    private val list: MutableList<T> = mutableListOf()

    operator fun T.unaryMinus() {
        list.add(this)
    }

    override fun build(): List<T> {
        return list;
    }
}