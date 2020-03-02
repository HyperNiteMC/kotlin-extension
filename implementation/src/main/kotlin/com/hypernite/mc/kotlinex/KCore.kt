package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigBuilder
import com.hypernite.mc.hnmc.core.config.ConfigFactory
import com.hypernite.mc.hnmc.core.managers.SQLDataSource
import org.apache.commons.lang.Validate
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger

object KCore : KCoreAPI {

    private lateinit var db: Database

    override val argumentParser: ArgumentParser
        get() = ArgumentParserImpl

    override fun forKotlin(factory: ConfigFactory): ConfigFactory {
        val builder = factory as ConfigBuilder
        val plugin = builder::class.java.getDeclaredField("plugin").let {
            it.isAccessible = true
            it.get(builder) as JavaPlugin
        }
        return KotlinConfigFactory(plugin)
    }

    override fun <T> transaction(t: Transaction.() -> T): T {
        Validate.notNull(db, "SQLPool Database is null, maybe not initialized?")
        return org.jetbrains.exposed.sql.transactions.transaction(db, t)
    }

    internal fun setupMySQL(sql: SQLDataSource) {
        sql.dataSource ?: throw IllegalStateException("DataSource has not initialized")
        db = Database.connect(sql.dataSource)
        org.jetbrains.exposed.sql.transactions.transaction(db) {
            addLogger(StdOutSqlLogger)
        }
    }
}