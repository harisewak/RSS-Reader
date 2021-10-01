package com.harisewak.rssreader.common

import com.harisewak.rssreader.data.model.RSSChannel

sealed class Result(
    val channel: RSSChannel? = null,
    val error: String? = null
)

class Success(channel: RSSChannel) : Result(channel = channel)
class Failure(error: String) : Result(error = error)

