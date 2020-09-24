package com.hypernite.mc.kotlinex

import com.hypernite.mc.hnmc.core.main.HyperNiteMC
import org.jetbrains.exposed.sql.Transaction

class AsyncPromise<T>(
        private val run: Transaction.() -> T
) : AsyncInvoker<T> {

    lateinit var thenRun: (T) -> Unit
    lateinit var catchRun: (Throwable) -> Unit


    init {
        start()
    }

    private fun start() {
        val sch = HyperNiteMC.getAPI().coreScheduler
        sch.runAsync {
            try {
                val response = KCore.transaction(run)
                sch.runTask {
                    try {
                        if (this@AsyncPromise::thenRun.isInitialized) {
                            thenRun(response)
                        }
                    } catch (e: Throwable) {
                        if (this@AsyncPromise::catchRun.isInitialized) {
                            catchRun(e)
                        } else {
                            throw IllegalStateException("Uncaught Error: ${e.message}", e)
                        }
                    }
                }
            } catch (e: Throwable) {
                if (this@AsyncPromise::catchRun.isInitialized) {
                    catchRun(e)
                } else {
                    throw IllegalStateException("Uncaught Error: ${e.message}", e)
                }
            }
        }
    }

    override fun then(run: (T) -> Unit): AsyncInvoker<T> {
        this.thenRun = run
        return this
    }

    override fun catch(catch: (Throwable) -> Unit) {
        this.catchRun = catch
    }

}