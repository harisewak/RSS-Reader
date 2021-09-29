package com.harisewak.rssreader.data.repository

import android.util.Log
import com.harisewak.rssreader.di.DependencyProvider
import com.prof.rssparser.Channel

const val TAG = "FeedsRepo"
class FeedsRepository(

) {

    suspend fun getFeeds(
        url: String
    ) : Channel {
        Log.d(TAG, "getFeeds: called")

        return try {
            DependencyProvider
                .xmlParser()
                .getChannel(url)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception
            throw Error("Error occurred while trying to read RSS Feed")
        }
    }
}