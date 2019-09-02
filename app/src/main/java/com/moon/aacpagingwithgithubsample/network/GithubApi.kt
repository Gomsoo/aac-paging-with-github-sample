package com.moon.aacpagingwithgithubsample.network

import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("repositories")
    fun getRepos(@Query("since") since: Long? = null): Single<List<GithubRepo>>

    companion object {
        private const val GITHUB_API_BASE_URL = "https://api.github.com/"

        fun create(): GithubApi = Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}