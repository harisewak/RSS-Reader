package com.harisewak.rssreader.data.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "feed_bookmark_state"
)
data class FeedBookmarkState(
    @PrimaryKey
    val guid: String,
    val isBookmarked: Boolean = false
)
