package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory
import org.jetbrains.exposed.sql.Transaction

interface KCoreAPI {

    val argumentParser: ArgumentParser

    fun forKotlin(factory: ConfigFactory): ConfigFactory

    fun <T> transaction(t: Transaction.() -> T): T

}