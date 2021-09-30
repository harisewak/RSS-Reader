package com.harisewak.rssreader.presentation.feeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.harisewak.rssreader.databinding.FragmentFeedsBinding
import com.harisewak.rssreader.di.DependencyProvider
import kotlinx.coroutines.launch

const val TAG = "FeedsFragment"

class FeedsFragment : Fragment() {
    private lateinit var binding: FragmentFeedsBinding
    private val mAdapter: FeedsAdapter by lazy {
        DependencyProvider.feedsAdapter()
    }
    private val viewModel: FeedsViewModel by lazy {
        DependencyProvider.feedsViewModel(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapterListeners()

        bindAdapter()

        observeData()
    }

    private fun observeData() {
        viewModel.channel.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: channel name -> ${it.title}")

            mAdapter.submitList(it.rssFeeds)
        }
    }

    private fun bindAdapter() {
        binding.rvFeeds.apply {
            adapter = mAdapter
            addItemDecoration(
                DependencyProvider.divider()
            )
        }
    }

    private fun setAdapterListeners() {
        mAdapter.setClickListener { article ->

            viewModel.setCurrentArticle(article)

            findNavController()
                .navigate(
                    FeedsFragmentDirections
                        .actionFeedsFragmentToFeedDetailFragment()
                )
        }

        mAdapter.setBookmarkedListener { guid, bookmarkedAction ->
            lifecycleScope.launch {
                viewModel.updateBookmarkStatus(guid, true)
                bookmarkedAction.invoke()
            }
        }

        mAdapter.setUnBookmarkedListener { guid, unBookmarkedAction ->
            lifecycleScope.launch {
                viewModel.updateBookmarkStatus(guid, false)
                unBookmarkedAction.invoke()
            }
        }
    }

}