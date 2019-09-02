package com.moon.aacpagingwithgithubsample.extensions

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T : Any> Flowable<T>.observeOnUI(): Flowable<T> = observeOn(AndroidSchedulers.mainThread())
