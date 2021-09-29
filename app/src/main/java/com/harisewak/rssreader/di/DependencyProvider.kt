package com.harisewak.rssreader.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.harisewak.rssreader.RSSReaderApp
import com.harisewak.rssreader.common.Constants
import com.harisewak.rssreader.data.repository.FeedsRepository
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import com.harisewak.rssreader.presentation.feeds.FeedsAdapter
import com.harisewak.rssreader.presentation.feeds.FeedsViewModel
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import java.nio.charset.Charset


object DependencyProvider {
    private var feedsViewModel: FeedsViewModel? = null

    fun appContext() = RSSReaderApp.context ?: throw Error("Context is null from RSSReaderApp")

    fun xmlParser() = Parser.Builder()
        .context(appContext())
        .charset(Charset.forName("ISO-8859-7"))
        .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
        .build()

    fun feedsUseCase() = GetFeedsUseCase(FeedsRepository())

    fun feedsAdapter() = FeedsAdapter()
    fun divider() = DividerItemDecoration(
        appContext(),
        LinearLayoutManager.VERTICAL
    )

    fun feedsViewModel(activity: FragmentActivity): FeedsViewModel {
        return feedsViewModel
            ?: ViewModelProvider(
                activity,
                FeedsViewModel.Companion.FeedsViewModelFactory(
                    feedsUseCase(),
                    // putting this as default url for now
                    Constants.URL_FEED_BACKCHANNEL
                )
            ).get(FeedsViewModel::class.java)
    }
}