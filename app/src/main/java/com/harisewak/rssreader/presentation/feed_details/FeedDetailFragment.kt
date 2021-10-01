package com.harisewak.rssreader.presentation.feed_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.harisewak.rssreader.R
import com.harisewak.rssreader.common.toHtml
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.databinding.FragmentFeedDetailBinding
import com.harisewak.rssreader.di.DependencyProvider
import com.harisewak.rssreader.presentation.feeds.FeedsViewModel
import kotlinx.coroutines.launch
import android.content.Intent




const val TAG = "FeedDetailFragment"

class FeedDetailFragment : Fragment() {
    private lateinit var binding: FragmentFeedDetailBinding
    private val viewModel: FeedsViewModel by lazy {
        DependencyProvider.feedsViewModel(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.article.observe(viewLifecycleOwner) { article ->
            Log.d(TAG, "onViewCreated: article title -> ${article.title}")

            setData(article)
        }

    }

    private fun setData(article: RssFeed) {
        with(binding) {
            tvHeadline.text = article.title
            tvContent.text = article.content.toHtml()
            ivFeedImage.load(article.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.im_default_feed_image)
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
                lifecycleScope.launch {
                    if (article.isBookmarked) {
                        // update status as unbookmarked in db
                        viewModel.updateBookmarkStatus(article.guid, false)
                        // then update UI to reflect the same
                        ivBookmark.setImageResource(R.drawable.ic_unbookmarked)
                        article.isBookmarked = false
                    } else {
                        // update status as bookmarked in db
                        viewModel.updateBookmarkStatus(article.guid, true)
                        // then update UI to reflect the same
                        ivBookmark.setImageResource(R.drawable.ic_bookmarked)
                        article.isBookmarked = true
                    }

                }
            }

            btShare.setOnClickListener {
                shareFeed(article)
            }

        }
    }

    private fun shareFeed(article: RssFeed) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, article.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.link)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share_via)))
    }
}