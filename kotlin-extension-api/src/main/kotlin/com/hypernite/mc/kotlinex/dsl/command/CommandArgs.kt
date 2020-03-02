package com.hypernite.mc.kotlinex.dsl.command

class CommandArgs(args: Iterator<String>) : Iterator<String> by args {

    fun throwError(message: String): Nothing {
        throw ArgumentParseException(message)
    }

    internal class ArgumentParseException(message: String) : Exception("§4系統// §c$message")
}