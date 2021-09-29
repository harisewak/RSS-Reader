package com.harisewak.rssreader.di

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.harisewak.rssreader.RSSReaderApp
import com.harisewak.rssreader.data.repository.FeedsRepository
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import com.harisewak.rssreader.presentation.feeds.FeedsAdapter
import com.prof.rssparser.Parser
import java.nio.charset.Charset


object DependencyProvider {

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
}