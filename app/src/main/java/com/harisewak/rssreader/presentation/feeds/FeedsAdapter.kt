package com.harisewak.rssreader.presentation.feeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harisewak.rssreader.databinding.ItemFeedViewBinding
import com.prof.rssparser.Article

class FeedsAdapter : ListAdapter<Article, FeedItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val binding = ItemFeedViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeedItemViewHolder(
            binding
        )

    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {

        val DIFF_CALLBACK =

            object : DiffUtil.ItemCallback<Article>() {

                override
                fun areItemsTheSame(
                    oldArticle: Article, newArticle: Article
                ): Boolean {
                    return oldArticle.guid == newArticle.guid
                }

                override
                fun areContentsTheSame(
                    oldArticle: Article, newArticle: Article
                ): Boolean {
                    return oldArticle.equals(newArticle)
                }
            }
    }

}
