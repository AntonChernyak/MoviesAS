package ru.mikhailskiy.intensiv.extensions

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.threadSwitch(): Observable<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.threadSwitch(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.addLoader(progressBar: ProgressBar): Observable<T> =
    this.doOnSubscribe { progressBar.visibility = VISIBLE }
        .doFinally { progressBar.visibility = GONE }

fun <T> Single<T>.addLoader(progressBar: ProgressBar): Single<T> =
    this.doOnSubscribe { progressBar.visibility = VISIBLE }
        .doFinally { progressBar.visibility = GONE }