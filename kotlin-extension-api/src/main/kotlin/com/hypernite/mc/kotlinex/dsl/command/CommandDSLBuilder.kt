package com.hypernite.mc.kotlinex.dsl.command

import com.hypernite.mc.hnmc.core.managers.builder.Buildable
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode
import com.hypernite.mc.hnmc.core.misc.commands.CommandNodeBuilder
import com.hypernite.mc.hnmc.core.misc.commands.DefaultCommandBuilder
import com.hypernite.mc.kotlinex.dsl.ListDSLBuilder
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass

class CommandDSLBuilder(private val name: String) : Buildable<CommandNode> {

    private val alias: MutableList<String> = mutableListOf()
    private var description: String = ""
    private var permission: String? = null
    private var execute: (CommandSender, List<String>) -> Boolean = { _, _ -> false }
    private var tabCompleter: (CommandSender, List<String>) -> List<String>? = { _, _ -> null }
    private val placeholders: MutableList<String> = mutableListOf()
    private val child: MutableList<CommandNode> = mutableListOf()
    private val map: MutableMap<String, KClass<*>> = mutableMapOf()
    private val property: MutableMap<String, PlaceholderDSLBuilder.Property> = mutableMapOf()

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

    fun child(setup: CommandListDSLBuilder.() -> Unit) {
        val listDSLBuilder = CommandListDSLBuilder()
        listDSLBuilder.setup()
        child += listDSLBuilder.build()
    }

    fun placeholder(setup: PlaceholderDSLBuilder.() -> Unit) {
        val papiBuilder = PlaceholderDSLBuilder()
        papiBuilder.setup()
        map += papiBuilder.build()
        property += papiBuilder.property()
        placeholders += papiBuilder.toPlaceholders
    }

    fun placeholders(vararg args: String) {
        placeholders += args
    }


    fun execute(setup: CommandDSLExecutor.() -> Boolean) {
        this.execute = { sender, args ->
            try {
                val executor = CommandDSLExecutor(sender, map, property, args.iterator())
                executor.setup()
            } catch (e: CommandArgs.ArgumentParseException) {
                sender.sendMessage(e.message ?: "null")
            }
            false
        }
    }

    fun tabComplete(setup: ListDSLBuilder<String>.(CommandSender, List<String>) -> Unit) {
        val listBuilder = ListDSLBuilder<String>()
        this.tabCompleter = { sender, args ->
            listBuilder.setup(sender, args)
            listBuilder.build()
        }
    }


    override fun build(): CommandNode {
        return if (child.isNotEmpty()) {
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
                    .placeholder(if (placeholders.isEmpty()) null else placeholders.joinToString(" "))
                    .alias(*alias.toTypedArray())
                    .execute(execute)
                    .tabComplete(tabCompleter)
                    .build()
        }

    }
}