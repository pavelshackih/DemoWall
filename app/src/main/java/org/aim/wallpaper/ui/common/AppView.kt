package org.aim.wallpaper.ui.common

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface AppView : MvpView {

    @StateStrategyType(value = SingleStateStrategy::class)
    fun showError(error: Throwable)
}