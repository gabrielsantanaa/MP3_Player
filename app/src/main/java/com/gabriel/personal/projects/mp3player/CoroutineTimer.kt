package com.gabriel.personal.projects.mp3player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineTimer(private val countDown: Long, private val delay: Long) {

    //in millis
    private var currentDuration: Long = 0L
    private var isPaused = false

    fun pause() {
        isPaused = true
    }

    fun play(coroutineScope: CoroutineScope, tick: (time: Long) -> Unit) {
        coroutineScope.launch {
            isPaused = false
            while (!isPaused && currentDuration < countDown) {
                delay(delay)
                currentDuration += delay
                tick(currentDuration)
            }
        }
    }

    fun complete() {
        isPaused = true
        currentDuration = 0L
    }

}