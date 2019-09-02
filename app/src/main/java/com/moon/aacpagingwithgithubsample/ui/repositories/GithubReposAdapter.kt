package com.moon.aacpagingwithgithubsample.ui.repositories

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moon.aacpagingwithgithubsample.R
import com.moon.aacpagingwithgithubsample.data.vo.GithubRepo
import com.moon.aacpagingwithgithubsample.extensions.inflate
import com.moon.aacpagingwithgithubsample.extensions.setIndeterminateColor
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_repository.view.*

class GithubReposAdapter : PagedListAdapter<GithubRepo, RecyclerView.ViewHolder>(DIFFER_CONFIG) {

    companion object {
        private const val VIEW_TYPE_LOADING = R.layout.item_loading
        private const val VIEW_TYPE_REPOSITORY = R.layout.item_repository

        private val DIFFER_CONFIG = AsyncDifferConfig.Builder(
            object : DiffUtil.ItemCallback<GithubRepo>() {
                override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean =
                    oldItem == newItem
            }).build()
    }

    init {
        setHasStableIds(true)
    }

    var showLoadingAfter: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    notifyItemInserted(super.getItemCount())
                } else {
                    notifyItemRemoved(super.getItemCount())
                }
            }
        }

    override fun getItemCount(): Int = super.getItemCount() + if (showLoadingAfter) 1 else 0

    override fun getItemViewType(position: Int): Int = when {
        showLoadingAfter && position == super.getItemCount() -> VIEW_TYPE_LOADING
        else -> VIEW_TYPE_REPOSITORY
    }

    override fun getItemId(position: Int): Long = when {
        showLoadingAfter && position == super.getItemCount() -> Long.MIN_VALUE
        else -> getItem(position)?.id ?: RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.inflate(viewType).let {
            when (viewType) {
                VIEW_TYPE_REPOSITORY -> RepositoryViewHolder(it)
                else -> LoadingViewHolder(it)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_REPOSITORY ->
                getItem(position)?.let { (holder as? RepositoryViewHolder)?.bind(it) }
        }
    }

    private fun RepositoryViewHolder.bind(item: GithubRepo) {
        nameView.text = item.name
        fullNameView.text = item.fullName
    }

    private class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.nameView
        val fullNameView: TextView = itemView.fullNameView
    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.loadingProgressBar.setIndeterminateColor(
                ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            )
        }
    }
}