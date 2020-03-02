package com.hypernite.mc.kotlinex.dsl.command

class CommandArgs(args: Iterator<String>) : Iterator<String> by args {

    fun throwError(message: String): Exception {
        return ArgumentParseException(message)
    }

    class ArgumentParseException(message: String) : Exception("§4系統// §c$message")
}