package com.moon.aacpagingwithgithubsample.ui.repositories

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.moon.aacpagingwithgithubsample.EXTRA_PAGING_TYPE
import com.moon.aacpagingwithgithubsample.Injector
import com.moon.aacpagingwithgithubsample.R
import com.moon.aacpagingwithgithubsample.data.repository.GithubReposRepository
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import com.moon.aacpagingwithgithubsample.extensions.observeOnUI
import com.moon.aacpagingwithgithubsample.extensions.setIndeterminateColor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_repositories.*

class GithubReposActivity : AppCompatActivity() {

    private val disposables by lazy { CompositeDisposable() }

    private lateinit var viewModel: GithubReposViewModel
    private lateinit var adapter: GithubReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeViews()
        initializeViewModel()
    }

    private fun initializeViews() {
        repositoriesRecyclerView.adapter = GithubReposAdapter().also { adapter = it }
        initialLoadingProgressBar
            .setIndeterminateColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    private fun initializeViewModel() {
        val viewModelFactory = Injector.provideRepositoriesViewModelFactory(
            this,
            intent.getIntExtra(EXTRA_PAGING_TYPE, GithubReposRepository.NETWORK_AND_DATABASE)
        )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GithubReposViewModel::class.java)
                .apply {
                    items.observeOnUI()
                        .subscribeBy(onNext = ::submitItems, onError = {
                            // handle error
                        })
                        .addTo(disposables)

                    loadingInitial.distinctUntilChanged()
                        .observeOnUI()
                        .subscribeBy(
                            onNext = { initialLoadingProgressBar.isVisible = it },
                            onError = {
                                // handle error
                            })
                        .addTo(disposables)

                    loadingAfter.distinctUntilChanged()
                        .observeOnUI()
                        .subscribeBy(onNext = { adapter.showLoadingAfter = it }, onError = {
                            // handle error
                        })
                        .addTo(disposables)
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitItems(items: PagedList<GithubRepo>) {
        adapter.submitList(items)
    }
}