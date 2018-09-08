package org.aim.wallpaper.data.billing

import io.reactivex.Single

interface BillingWrapper {

    fun isUserAlreadyPayed(): Single<Boolean>

    fun getProducts(): Single<List<String>>
}