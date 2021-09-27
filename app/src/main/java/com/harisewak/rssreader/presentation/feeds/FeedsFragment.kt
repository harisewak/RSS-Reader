package com.harisewak.rssreader.presentation.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harisewak.rssreader.databinding.FragmentFeedsBinding

class FeedsFragment: Fragment() {
    private lateinit var binding: FragmentFeedsBinding

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
}