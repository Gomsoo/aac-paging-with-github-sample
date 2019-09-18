package com.moon.aacpagingwithgithubsample.ui.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.moon.aacpagingwithgithubsample.data.repository.GithubReposRepository
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import io.reactivex.Flowable

class GithubReposViewModel(repository: GithubReposRepository) : ViewModel() {

    private val listing = repository.getRepos(PAGE_SIZE)
    val items: Flowable<PagedList<GithubRepo>> = listing.items
    val loadingInitial: Flowable<Boolean> = listing.loadingInitial
    val loadingAfter: Flowable<Boolean> = listing.loadingAfter

    companion object {
        private const val PAGE_SIZE = 20
    }
}

class GithubReposViewModelFactory(
    private val repository: GithubReposRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GithubReposViewModel(repository) as T
}
