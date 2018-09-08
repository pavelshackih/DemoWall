package org.aim.wallpaper.ext

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.aim.wallpaper.executors.RxExecutors

fun <T> maybeFromNull(value: T?): Maybe<T> = if (value == null) Maybe.empty() else Maybe.just(value)

fun <T> Single<T>.executeOn(rxExecutors: RxExecutors): Single<T> {
    return this.subscribeOn(rxExecutors.work).observeOn(rxExecutors.main)
}

fun <T> Observable<T>.executeOn(rxExecutors: RxExecutors): Observable<T> {
    return this.subscribeOn(rxExecutors.work).observeOn(rxExecutors.main)
}

fun Completable.executeOn(rxExecutors: RxExecutors): Completable {
    return this.subscribeOn(rxExecutors.work).observeOn(rxExecutors.main)
}

fun <T> Maybe<T>.executeOn(rxExecutors: RxExecutors): Maybe<T> {
    return this.subscribeOn(rxExecutors.work).observeOn(rxExecutors.main)
}