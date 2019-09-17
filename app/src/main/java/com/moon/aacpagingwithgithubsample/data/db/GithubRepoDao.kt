package com.moon.aacpagingwithgithubsample.data.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo

@Dao
interface GithubRepoDao : BaseDao<GithubRepo> {

    @Query("SELECT * FROM github_repos")
    fun getGithubRepos(): DataSource.Factory<Int, GithubRepo>
}
