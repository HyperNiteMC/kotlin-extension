package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory

internal object KCore : KCoreAPI {
    override val argumentParser: ArgumentParser
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun forKotlin(factory: ConfigFactory): ConfigFactory {
        throw Exception("trying to run API.jar into server")
    }
}