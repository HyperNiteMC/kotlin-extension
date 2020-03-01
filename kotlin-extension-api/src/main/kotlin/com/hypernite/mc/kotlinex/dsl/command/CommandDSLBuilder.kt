package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode
import com.hypernite.mc.hnmc.core.misc.commands.CommandNodeBuilder
import com.hypernite.mc.hnmc.core.misc.commands.DefaultCommandBuilder
import com.hypernite.mc.kotlinex.dsl.ListDSLBuilder
import org.bukkit.command.CommandSender
import org.bukkit.inventory.Inventory
import kotlin.reflect.KClass

class CommandDSLBuilder(private val name: String) : Buildable<CommandNode> {

    private val alias: MutableList<String> = mutableListOf()
    private var description: String = ""
    private var permission: String? = null
    private var execute: (CommandSender) -> Unit = {}
    private var tabCompleter: (CommandSender) -> List<String>? = { null }
    private val child: MutableList<CommandNode> = mutableListOf()
    private val map: MutableMap<String, KClass<*>> = mutableMapOf()

    fun alias(setup: ListDSLBuilder<String>.() -> Unit) {
        val listDSLBuilder = ListDSLBuilder<String>()
        listDSLBuilder.setup()
        alias += listDSLBuilder.build()
    }

    fun alias(vararg alias: String) {
        this.alias += alias
    }

    fun description(description: String) {
        this.description = description
    }

    fun permission(permission: String) {
        this.permission = permission
    }

    fun child(setup: CommandListDSLBuilder.() -> Unit){
        val listDSLBuilder = CommandListDSLBuilder()
        listDSLBuilder.setup()
        child += listDSLBuilder.build()
    }

    fun placeholder(setup: PlaceholderDSLBuilder.() -> Unit){
        val papiBuilder = PlaceholderDSLBuilder()
        papiBuilder.setup()
        map += papiBuilder.build()
    }



    override fun build(): CommandNode {
        return if (child.isNotEmpty()){
            DefaultCommandBuilder(name)
                    .description(description)
                    .permission(permission)
                    .alias(*alias.toTypedArray())
                    .children(*child.toTypedArray())
                    .build()
        }else{
            CommandNodeBuilder(name)
                    .description(description)
                    .permission(permission)
                    .alias(*alias.toTypedArray())
                    .execute { sender, args ->

                        true
                    }
                    .tabComplete { sender, args ->
                        null
                    }
                    .build()
        }

    }
}