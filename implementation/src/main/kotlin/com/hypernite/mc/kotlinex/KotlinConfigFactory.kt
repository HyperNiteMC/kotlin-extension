package com.hypernite.mc.kotlinex

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hypernite.mc.hnmc.core.config.ConfigFactory
import com.hypernite.mc.hnmc.core.config.yaml.Configuration
import com.hypernite.mc.hnmc.core.config.yaml.Resource
import com.hypernite.mc.hnmc.core.managers.YamlManager
import org.bukkit.plugin.java.JavaPlugin

class KotlinConfigFactory(private val plugin: JavaPlugin) : ConfigFactory {

    private val map: MutableMap<String, Class<out Configuration>> = mutableMapOf()

    override fun register(p0: String, p1: Class<out Configuration>): ConfigFactory {
        map[p0] = p1
        return this
    }

    override fun register(p0: Class<out Configuration>): ConfigFactory {
        p0.getAnnotation(Resource::class.java)?.also { map[it.locate] = p0 }
                ?: throw IllegalStateException("找不到 Resource 標註")
        return this
    }

    override fun dump(): YamlManager {
        val construct = Class.forName("com.hypernite.mc.hnmc.core.config.YamlHandler").getConstructor(Map::class.java, JavaPlugin::class.java, Boolean::class.java, KotlinModule::class.java.superclass)
        construct.isAccessible = true
        return (construct.newInstance(map, plugin, true, KotlinModule()) as YamlManager).also {
            plugin.logger.info("正在初始化 yml (kotlin)")
            it.reloadConfigs()
        }
    }


}