package com.dawn.kotlinbasedemo

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")

fun main() {
    runBlocking {
        val handler = CoroutineExceptionHandler { context, exception ->
            log("handler $context")
            log("CoroutineExceptionHandler got $exception")
        }
        val job = GlobalScope.launch(handler) { // root coroutine, running in GlobalScope
            log("outer $coroutineContext")
            test()
            log("outer end")
        }
        job.join()
        log("end")
    }
}

suspend fun test() {
    coroutineScope {
        log("test $coroutineContext")
        val job = launch() {
            log("job $coroutineContext")
//            throw AssertionError()
            delay(1000)
        }

        val job2 = launch() {
            try {
                log("job2 $coroutineContext")
                delay(1000)
            } catch (e: CancellationException) {
                log("job2 $e")
            }
        }
    }
    log("test end")
}
