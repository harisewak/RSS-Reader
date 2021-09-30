package com.harisewak.rssreader.domain.usecase

import com.harisewak.rssreader.data.repository.FeedsRepository

class GetFeedsUseCase(
    private val repository: FeedsRepository
) {

    suspend fun getFeeds(url: String) = repository.getFeeds(url)

    suspend fun updateBookmarkStatus(guid: String, isBookmarked: Boolean) {
        repository.updateBookmarkStatus(guid, isBookmarked)
    }

}