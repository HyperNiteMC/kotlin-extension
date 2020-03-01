package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigBuilder
import com.hypernite.mc.hnmc.core.config.ConfigFactory
import org.bukkit.plugin.java.JavaPlugin

internal object KCore : KCoreAPI {
    override val argumentParser: ArgumentParser
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun forKotlin(factory: ConfigFactory): ConfigFactory {
        val builder = factory as ConfigBuilder
        val plugin = builder::class.java.getDeclaredField("plugin").let{
            it.isAccessible = true
            it.get(builder) as JavaPlugin
        }
        return KotlinConfigFactory(plugin)
    }
}