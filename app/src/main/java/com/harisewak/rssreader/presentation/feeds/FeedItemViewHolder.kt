package com.harisewak.rssreader.presentation.feeds

import androidx.recyclerview.widget.RecyclerView
import com.harisewak.rssreader.R
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.databinding.ItemFeedViewBinding

class FeedItemViewHolder(
    private val binding: ItemFeedViewBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        article: RssFeed,
        click: (article: RssFeed) -> Unit,
        bookmarkedAction: (guid: String, () -> Unit) -> Unit,
        unBookmarkedAction: (guid: String, () -> Unit) -> Unit
    ) {
        with(binding) {
            tvHeadline.text = article.title
            itemView.setOnClickListener {
                click.invoke(article)
            }

            if (article.isBookmarked) {
                ivBookmark.setImageResource(
                    R.drawable.ic_bookmarked
                )
            } else {
                ivBookmark.setImageResource(
                    R.drawable.ic_unbookmarked
                )
            }

            ivBookmark.setOnClickListener {
                if (article.isBookmarked) {
                    unBookmarkedAction.invoke(article.guid) {
                        ivBookmark.setImageResource(R.drawable.ic_unbookmarked)
                        article.isBookmarked = false
                    }

                } else {
                    bookmarkedAction.invoke(article.guid) {
                        ivBookmark.setImageResource(R.drawable.ic_bookmarked)
                        article.isBookmarked = true
                    }
                }
            }
        }
    }
}