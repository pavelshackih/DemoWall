package org.aim.wallpaper.executors

import io.reactivex.Scheduler

interface RxExecutors {
    val main: Scheduler
    val work: Scheduler
}