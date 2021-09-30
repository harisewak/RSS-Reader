package com.harisewak.rssreader.data.repository

import android.util.Log
import com.harisewak.rssreader.common.Constants
import com.harisewak.rssreader.data.model.RSSChannel
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.data.repository.local.BookmarkDao
import com.harisewak.rssreader.data.repository.local.FeedBookmarkState
import com.harisewak.rssreader.di.DependencyProvider
import com.prof.rssparser.Article
import com.prof.rssparser.Channel

const val TAG = "FeedsRepo"

class FeedsRepository(

) {

    suspend fun getFeeds(
        url: String
    ): RSSChannel {
        Log.d(TAG, "getFeeds: called")

        return try {
            val channel = DependencyProvider
                .xmlParser()
                .getChannel(url)

            // push data into our RssChannel
            val rssChannel = createRssChannel(channel)
            // expose our RssChannel
            rssChannel
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception
            throw Error("Error occurred while trying to read RSS Feed")
        }
    }


    private suspend fun createRssChannel(channel: Channel) = RSSChannel(
        channel.title!!,
        channel.description!!,
        channel.articles.map { article ->
            // inserting all new articles as unbookmarked
            // existing articles will be ignored
            DependencyProvider
                .bookmarkDao()
                .insert(FeedBookmarkState(article.guid!!))
            // returned mapped feed item
            createRssFeed(article)
        }
    )

    private suspend fun createRssFeed(article: Article) = RssFeed(
        article.guid!!,
        title = article.title!!,
        imageUrl = article.image!!,
        content = getContentOrDesc(article),
        isBookmarked = DependencyProvider
            .bookmarkDao()
//            .isBookmarked(article.guid!!)
            .isBookmarkedWithLog(article.guid!!)
    )

    suspend fun BookmarkDao.isBookmarkedWithLog(guid: String): Boolean {
        val isBookmarked = isBookmarked(guid)
        Log.d(TAG, "isBookmarkedWithLog: guid -> $guid, isBookmarked -> $isBookmarked")
        return isBookmarked
    }

    private fun getContentOrDesc(article: Article): String {

        article.content?.let {
            return it
        }
        article.description?.let {
            return it
        }

        return Constants.ERR_FEED_BODY_EMPTY
    }

    suspend fun updateBookmarkStatus(guid: String, isBookmarked: Boolean) {
        DependencyProvider
            .bookmarkDao()
            .updateBookmarkStatus(guid, isBookmarked)
    }

}