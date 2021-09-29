package com.harisewak.rssreader.presentation.feed_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.harisewak.rssreader.R
import com.harisewak.rssreader.common.withHtmlFormatting
import com.harisewak.rssreader.databinding.FragmentFeedDetailBinding
import com.harisewak.rssreader.di.DependencyProvider
import com.harisewak.rssreader.presentation.feeds.FeedsViewModel

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

            with(binding) {
                tvHeadline.text = article.title
                tvContent.text = article.content.withHtmlFormatting()
                ivFeedImage.load(article.image) {
                    crossfade(true)
                    placeholder(R.drawable.im_default_feed_image)
                }
            }
        }

    }
}