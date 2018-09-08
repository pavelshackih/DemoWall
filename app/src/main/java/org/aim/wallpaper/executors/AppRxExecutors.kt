package org.aim.wallpaper.executors

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppRxExecutors : RxExecutors {
    override val main: Scheduler = AndroidSchedulers.mainThread()
    override val work: Scheduler = Schedulers.io()
}