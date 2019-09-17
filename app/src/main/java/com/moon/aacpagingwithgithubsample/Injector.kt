package com.moon.aacpagingwithgithubsample

import android.content.Context
import com.moon.aacpagingwithgithubsample.data.db.AppDatabase
import com.moon.aacpagingwithgithubsample.data.repository.DbGithubReposRepository
import com.moon.aacpagingwithgithubsample.data.repository.GithubReposRepository
import com.moon.aacpagingwithgithubsample.data.repository.NetworkGithubReposRepository
import com.moon.aacpagingwithgithubsample.network.GithubApi
import com.moon.aacpagingwithgithubsample.ui.repositories.GithubReposViewModelFactory

object Injector {

    private fun getGithubRepoDao(context: Context) =
        AppDatabase.getInstance(context).githubRepoDao()

    private fun getRepositoriesRepository(context: Context, type: Int): GithubReposRepository =
        when (type) {
            GithubReposRepository.NETWORK_ONLY -> NetworkGithubReposRepository(GithubApi.create())
            else -> DbGithubReposRepository(GithubApi.create(), getGithubRepoDao(context))
        }

    fun provideRepositoriesViewModelFactory(
        context: Context,
        type: Int
    ): GithubReposViewModelFactory =
        GithubReposViewModelFactory(getRepositoriesRepository(context, type))
}
