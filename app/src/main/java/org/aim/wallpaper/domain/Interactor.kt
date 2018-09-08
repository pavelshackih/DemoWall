package org.aim.wallpaper.domain

import org.aim.wallpaper.executors.RxExecutors

interface Interactor {

    val rxExecutors: RxExecutors
}