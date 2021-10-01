package com.harisewak.rssreader.presentation.feeds

import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.harisewak.rssreader.R
import com.harisewak.rssreader.common.*
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_switch_channel, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_change_channel) {
            Log.d(TAG, "onOptionsItemSelected: ")
            showChangeChannelUi()
            return true
        }
        return false
    }

    private fun showChangeChannelUi() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_change_channel, binding.root, false)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()

        val etChangeChannel = view.findViewById<EditText>(R.id.et_change_channel)
        etChangeChannel
            .setOnEditorActionListener { _, _, _ ->
                // handle keyboard action
                changeChannelClicked(etChangeChannel.text.toString(), dialog)
                true
            }

        view.findViewById<Button>(R.id.bt_change_channel)
            .setOnClickListener {
                // handle button click
                changeChannelClicked(etChangeChannel.text.toString(), dialog)
            }

    }

    private fun changeChannelClicked(url: String, dialog: BottomSheetDialog) {
        if (URLUtil.isValidUrl(url)) {
            showProgress()
            // load new channel
            viewModel.url = url
            viewModel.getFeeds()
            binding.root.hideKeyboard()
            dialog.dismiss()
        } else {
            binding.root.showSnackbar(getString(R.string.msg_invalid_url))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        showProgress()

        viewModel.channel.observe(viewLifecycleOwner) { result ->

            when (result) {
                is Success -> {
                    result.channel?.let {
                        Log.d(TAG, "onViewCreated: channel name -> ${it.title}")
                        setTitle(it.title)
                        mAdapter.submitList(it.rssFeeds)
                    }
                }

                is Failure -> {
                    result.error?.let { error ->
                        binding.root.showSnackbarWithRetry(error) {
                            showProgress()
                            viewModel.getFeeds()
                        }
                    }
                }
            }

            hideProgress()
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