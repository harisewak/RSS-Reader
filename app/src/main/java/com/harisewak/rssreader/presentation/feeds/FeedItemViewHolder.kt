package com.harisewak.rssreader.presentation.feeds

import androidx.recyclerview.widget.RecyclerView
import com.harisewak.rssreader.databinding.ItemFeedViewBinding
import com.prof.rssparser.Article

class FeedItemViewHolder(
    private val binding: ItemFeedViewBinding,
    ) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article, click: (article: Article) -> Unit) {
        with(binding) {
            tvHeadline.text = article.title
            itemView.setOnClickListener {
                click.invoke(article)
            }
        }
    }
}