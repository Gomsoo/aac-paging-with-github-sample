package com.moon.aacpagingwithgithubsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moon.aacpagingwithgithubsample.data.repository.GithubReposRepository
import com.moon.aacpagingwithgithubsample.ui.repositories.GithubReposActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkOnlyButton.setOnClickListener {
            startRepositoriesActivity(GithubReposRepository.NETWORK_ONLY)
        }
        networkAndDatabaseButton.setOnClickListener {
            startRepositoriesActivity(GithubReposRepository.NETWORK_AND_DATABASE)
        }
    }

    private fun startRepositoriesActivity(type: Int) {
        startActivity(Intent(this, GithubReposActivity::class.java).apply {
            putExtra(EXTRA_PAGING_TYPE, type)
        })
    }
}
