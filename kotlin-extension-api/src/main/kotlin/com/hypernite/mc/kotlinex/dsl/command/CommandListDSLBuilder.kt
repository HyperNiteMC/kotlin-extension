package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode

class CommandListDSLBuilder : Buildable<List<CommandNode>> {

    private val list: MutableList<CommandNode> = mutableListOf()

    operator fun CommandNode.unaryPlus(){
        list += this
    }

    override fun build(): List<CommandNode> {
        return list
    }
}