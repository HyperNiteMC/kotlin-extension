package com.hypernite.mc.kotlinex

import com.hypernite.mc.kotlinex.dsl.command.CommandArgs
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass

interface ArgumentParser {

    fun <T : Any> registerParser(cls: KClass<T>, tags: Array<String>, parse: (CommandArgs, CommandSender) -> T)

    fun <T : Any> tryParse(cls: KClass<T>, args: Iterator<String>, sender: CommandSender): T

    fun getPlaceholders(cls: KClass<out Any>): Array<String>

}