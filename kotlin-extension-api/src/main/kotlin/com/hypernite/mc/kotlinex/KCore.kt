package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory
import org.jetbrains.exposed.sql.Transaction

object KCore : KCoreAPI {
    override val argumentParser: ArgumentParser
        get() = throw Exception("trying to run API.jar into server")

    override fun forKotlin(factory: ConfigFactory): ConfigFactory {
        throw Exception("trying to run API.jar into server")
    }

    override fun <T> transaction(t: Transaction.() -> T): T {
        throw Exception("trying to run API.jar into server")
    }

    override fun <T> asyncTransaction(t: Transaction.() -> T): AsyncInvoker<T> {
        throw Exception("trying to run API.jar into server")
    }

    override val singlePoolEnabled: Boolean
        get() = throw Exception("trying to run API.jar into server")
}