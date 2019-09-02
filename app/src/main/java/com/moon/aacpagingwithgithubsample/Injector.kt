package com.moon.aacpagingwithgithubsample

import com.moon.aacpagingwithgithubsample.data.repository.GithubReposRepository
import com.moon.aacpagingwithgithubsample.data.repository.NetworkGithubReposRepository
import com.moon.aacpagingwithgithubsample.network.GithubApi
import com.moon.aacpagingwithgithubsample.ui.repositories.GithubReposViewModelFactory

object Injector {

    private fun getRepositoriesRepository(type: Int): GithubReposRepository = when (type) {
        GithubReposRepository.NETWORK_ONLY -> NetworkGithubReposRepository(GithubApi.create())
        else -> TODO()
    }

    fun provideRepositoriesViewModelFactory(type: Int): GithubReposViewModelFactory =
        GithubReposViewModelFactory(getRepositoriesRepository(type))
}
