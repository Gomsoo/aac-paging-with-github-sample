package com.moon.aacpagingwithgithubsample.data.repository

import androidx.paging.PagedList
import io.reactivex.Flowable

data class Listing<T>(
    val items: Flowable<PagedList<T>>,
    val loadingInitial: Flowable<Boolean>,
    val loadingAfter: Flowable<Boolean>
)
