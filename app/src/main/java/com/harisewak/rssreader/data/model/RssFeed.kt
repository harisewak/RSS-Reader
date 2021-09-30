package com.harisewak.rssreader.data.model

data class RssFeed(
    val guid: String,
    val title: String,
    val imageUrl: String,
    val content: String,
    var isBookmarked: Boolean = false
)
