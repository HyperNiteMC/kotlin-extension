package com.hypernite.mc.kotlinex

interface AsyncInvoker<T> {

    fun then(run: (T) -> Unit): AsyncInvoker<T>

    fun catch(catch: (Throwable) -> Unit)
}