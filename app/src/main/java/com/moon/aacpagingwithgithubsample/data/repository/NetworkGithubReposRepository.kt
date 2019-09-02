package com.moon.aacpagingwithgithubsample.data.repository

import androidx.paging.DataSource
import androidx.paging.toFlowable
import com.moon.aacpagingwithgithubsample.data.source.ItemKeyedGithubReposDataSource
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import com.moon.aacpagingwithgithubsample.network.GithubApi
import io.reactivex.processors.BehaviorProcessor

class NetworkGithubReposRepository(private val githubApi: GithubApi) : GithubReposRepository {

    override fun getRepos(pageSize: Int): Listing<GithubRepo> {
        val dataSourceFactory = GithubReposDataSourceFactory(githubApi)
        val dataSource = dataSourceFactory.dataSource
        val pagedList = dataSourceFactory.toFlowable(pageSize = pageSize)
        return Listing(
            items = pagedList,
            loadingInitial = dataSource.switchMap { it.loadingInitial },
            loadingAfter = dataSource.switchMap { it.loadingAfter }
        )
    }

    private class GithubReposDataSourceFactory(
        private val githubApi: GithubApi
    ) : DataSource.Factory<Long, GithubRepo>() {
        val dataSource = BehaviorProcessor.create<ItemKeyedGithubReposDataSource>()
        override fun create(): DataSource<Long, GithubRepo> =
            ItemKeyedGithubReposDataSource(githubApi).also { dataSource.onNext(it) }
    }
}