package com.moon.aacpagingwithgithubsample.data.repository

import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo

interface GithubReposRepository {

    companion object {
        const val NETWORK_ONLY = 0
        const val NETWORK_AND_DATABASE = 1
    }

    fun getRepos(pageSize: Int): Listing<GithubRepo>
}