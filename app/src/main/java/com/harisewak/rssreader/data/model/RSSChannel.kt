package com.harisewak.rssreader.data.model

data class RSSChannel(
    val title: String,
    val description: String,
    val rssFeeds: List<RssFeed>
)
