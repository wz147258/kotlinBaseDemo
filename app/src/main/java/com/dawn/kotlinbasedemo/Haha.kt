package com.dawn.kotlinbasedemo

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.rxMaybe
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")

private val myDispatcher by lazy {
    Executors.newFixedThreadPool(10, object : ThreadFactory {
        val count = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "myThreadPool-${count.getAndAdd(1)}")
        }
    }).asCoroutineDispatcher()
}

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

        flow {
            delay(1000)
            emit("1")
            delay(1000)
            emit("2")
            delay(1000)
            emit("3")
            delay(1000)
            emit("4")
        }
            .onEach {
                log("emit:$it")
            }
            .flowOn(myDispatcher)
            .flowOn(Dispatchers.IO)
            .collect {
                log("collect:$it")
            }
    }
    log("runBlocking end")
    rxMaybe {

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
