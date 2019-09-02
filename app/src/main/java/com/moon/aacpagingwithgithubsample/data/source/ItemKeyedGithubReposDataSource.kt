package com.moon.aacpagingwithgithubsample.data.source

import androidx.paging.ItemKeyedDataSource
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import com.moon.aacpagingwithgithubsample.network.GithubApi
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.subscribeBy

class ItemKeyedGithubReposDataSource(
    private val githubApi: GithubApi
) : ItemKeyedDataSource<Long, GithubRepo>() {

    private var afterDisposable: Disposable? = null

    val loadingInitial: BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)
    val loadingAfter: BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<GithubRepo>
    ) {
        githubApi.getRepos()
            .doOnSubscribe { loadingInitial.onNext(true) }
            .onErrorReturn { emptyList() }
            .doOnSuccess { loadingInitial.onNext(false) }
            .blockingGet()
            .let(callback::onResult)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<GithubRepo>) {
        if (afterDisposable?.isDisposed != false) {
            githubApi.getRepos(params.key)
                .doOnSubscribe { loadingAfter.onNext(true) }
                .doAfterTerminate { loadingAfter.onNext(false) }
                .subscribeBy(onSuccess = callback::onResult, onError = {
                    // handle error
                })
                .also { afterDisposable = it }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<GithubRepo>) {
        // ignored, since we only ever append to our initial load
    }

    override fun getKey(item: GithubRepo): Long = item.id
}