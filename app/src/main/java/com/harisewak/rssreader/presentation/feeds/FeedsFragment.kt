package com.harisewak.rssreader.presentation.feeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.harisewak.rssreader.R
import com.harisewak.rssreader.common.Constants
import com.harisewak.rssreader.data.repository.FeedsRepository
import com.harisewak.rssreader.databinding.FragmentFeedsBinding
import com.harisewak.rssreader.databinding.ItemFeedViewBinding
import com.harisewak.rssreader.di.DependencyProvider
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import com.prof.rssparser.Article

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

        mAdapter.setListener { article ->

            viewModel.setCurrentArticle(article)

            findNavController()
                .navigate(
                    FeedsFragmentDirections
                        .actionFeedsFragmentToFeedDetailFragment()
                )
        }

        binding.rvFeeds.apply {
            adapter = mAdapter
            addItemDecoration(
                DependencyProvider.divider()
            )
        }


        viewModel.channel.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: channel name -> ${it.title}")

            mAdapter.submitList(it.articles)
        }
    }

}