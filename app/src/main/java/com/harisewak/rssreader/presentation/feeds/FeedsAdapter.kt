package com.harisewak.rssreader.presentation.feeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.databinding.ItemFeedViewBinding

class FeedsAdapter() : ListAdapter<RssFeed, FeedItemViewHolder>(DIFF_CALLBACK) {
    private lateinit var click: (article: RssFeed) -> Unit
    private lateinit var bookmarkedAction: (guid: String, () -> Unit) -> Unit
    private lateinit var unBookmarkedAction: (guid: String, () -> Unit) -> Unit
    fun setClickListener(click: (article: RssFeed) -> Unit) {
        this.click = click
    }
    fun setBookmarkedListener(click: (guid: String, () -> Unit) -> Unit) {
        this.bookmarkedAction = click
    }
    fun setUnBookmarkedListener(click: (guid: String, () -> Unit) -> Unit) {
        this.unBookmarkedAction = click
    }

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
        holder.bind(
            currentList[position],
            click,
            bookmarkedAction,
            unBookmarkedAction
        )
    }


    companion object {

        val DIFF_CALLBACK =

            object : DiffUtil.ItemCallback<RssFeed>() {

                override
                fun areItemsTheSame(
                    oldRssFeed: RssFeed, newRssFeed: RssFeed
                ): Boolean {
                    return oldRssFeed.guid == newRssFeed.guid
                }

                override
                fun areContentsTheSame(
                    oldRssFeed: RssFeed, newRssFeed: RssFeed
                ): Boolean {
                    return oldRssFeed.equals(newRssFeed)
                }
            }
    }

}
