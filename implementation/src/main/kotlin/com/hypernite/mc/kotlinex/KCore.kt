package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.config.ConfigFactory
import com.hypernite.mc.hnmc.core.managers.SQLDataSource
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
        val plugin = factory::class.java.getDeclaredField("plugin").let {
            it.isAccessible = true
            it.get(factory) as JavaPlugin
        }
        return KotlinConfigFactory(plugin)
    }

    override fun <T> transaction(t: Transaction.() -> T): T {
        if (!singlePoolEnabled) throw IllegalStateException("SQLPool Database is null, maybe not initialized?")
        return org.jetbrains.exposed.sql.transactions.transaction(db, t)
    }

    override fun <T> asyncTransaction(t: Transaction.() -> T): AsyncInvoker<T> {
        if (!singlePoolEnabled) throw IllegalStateException("SQLPool Database is null, maybe not initialized?")
        return AsyncPromise(t)
    }

    override val singlePoolEnabled: Boolean
        get() = this::db.isInitialized

    internal fun setupMySQL(sql: SQLDataSource) {
        sql.dataSource ?: throw IllegalStateException("DataSource has not initialized")
        db = Database.connect(sql.dataSource)
        org.jetbrains.exposed.sql.transactions.transaction(db) {
            addLogger(StdOutSqlLogger)
        }
    }
}