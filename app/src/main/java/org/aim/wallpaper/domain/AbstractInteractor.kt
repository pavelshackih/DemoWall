package org.aim.wallpaper.domain

import org.aim.wallpaper.executors.RxExecutors

abstract class AbstractInteractor(override val rxExecutors: RxExecutors) : Interactor