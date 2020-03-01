package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory

interface KCoreAPI {

    val argumentParser: ArgumentParser

    fun forKotlin(factory: ConfigFactory): ConfigFactory


}