package com.moon.aacpagingwithgithubsample.data.repository

import androidx.paging.PagedList
import androidx.paging.toFlowable
import com.moon.aacpagingwithgithubsample.data.db.GithubRepoDao
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import com.moon.aacpagingwithgithubsample.network.GithubApi
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.subscribeBy

class DbGithubReposRepository(
    private val githubApi: GithubApi,
    private val githubRepoDao: GithubRepoDao
) : GithubReposRepository {

    override fun getRepos(pageSize: Int): Listing<GithubRepo> {
        val dataSourceFactory = githubRepoDao.getGithubRepos()
        val boundaryCallback =
            BoundaryCallback(githubApi, githubRepoDao)
        val pagedList =
            dataSourceFactory.toFlowable(pageSize = pageSize, boundaryCallback = boundaryCallback)
        return Listing(
            items = pagedList,
            loadingInitial = boundaryCallback.loadingInitial,
            loadingAfter = boundaryCallback.loadingAfter
        )
    }
}

private class BoundaryCallback(
    private val githubApi: GithubApi,
    private val githubRepoDao: GithubRepoDao
) : PagedList.BoundaryCallback<GithubRepo>() {

    private var initialDisposable: Disposable? = null
    private var afterDisposable: Disposable? = null

    val loadingInitial: BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)
    val loadingAfter: BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)

    override fun onZeroItemsLoaded() {
        if (initialDisposable?.isDisposed == false) return
        githubApi.getRepos()
            .doOnSubscribe { loadingInitial.onNext(true) }
            .doOnSuccess { loadingInitial.onNext(false) }
            .subscribeBy(
                onSuccess = { githubRepoDao.insert(it) },
                onError = {
                    // handle error
                }
            )
            .also { initialDisposable = it }
    }

    override fun onItemAtEndLoaded(itemAtEnd: GithubRepo) {
        if (afterDisposable?.isDisposed == false) return
        githubApi.getRepos(itemAtEnd.id)
            .doOnSubscribe { loadingAfter.onNext(true) }
            .doAfterTerminate { loadingAfter.onNext(false) }
            .subscribeBy(
                onSuccess = { githubRepoDao.insert(it) },
                onError = {
                    // handle error
                }
            )
            .also { afterDisposable = it }
    }

    override fun onItemAtFrontLoaded(itemAtFront: GithubRepo) {
        // ignored, since we only ever append to what's in the DB
    }
}