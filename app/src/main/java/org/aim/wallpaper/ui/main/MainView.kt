package org.aim.wallpaper.ui.main

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import org.aim.wallpaper.domain.Album
import org.aim.wallpaper.ui.common.AppView

interface MainView : AppView {

    @StateStrategyType(value = SingleStateStrategy::class)
    fun showList(list: List<Album>)
}