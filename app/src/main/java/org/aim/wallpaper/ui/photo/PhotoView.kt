package org.aim.wallpaper.ui.photo

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.ui.common.AppView

interface PhotoView : AppView {

    fun showList(list: List<Photo>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgressDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgressDialog()
}
