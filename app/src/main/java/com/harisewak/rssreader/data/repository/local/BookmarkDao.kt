package com.harisewak.rssreader.data.repository.local

import androidx.room.*

@Dao
interface BookmarkDao {
    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insert(bookmarkState: FeedBookmarkState)

    @Query("UPDATE feed_bookmark_state SET isBookmarked=:isBookmarked WHERE guid = :guid")
    suspend fun updateBookmarkStatus(guid: String, isBookmarked: Boolean)

    @Query(
        """SELECT EXISTS(
                SELECT * 
                FROM feed_bookmark_state
                WHERE guid=:guid
                AND isBookmarked=1)"""
    )
    suspend fun isBookmarked(guid: String): Boolean
}