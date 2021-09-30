package com.harisewak.rssreader.presentation.feeds

import androidx.lifecycle.*
import com.harisewak.rssreader.data.model.RSSChannel
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedsViewModel(
    private val useCase: GetFeedsUseCase,
    private val url: String
) : ViewModel() {

    fun setCurrentArticle(article: RssFeed) {
        _article.value = article
    }

    suspend fun updateBookmarkStatus(guid: String, isBookmarked: Boolean) {
        useCase.updateBookmarkStatus(guid, isBookmarked)
    }

    private val _channel = MutableLiveData<RSSChannel>()
    val channel: LiveData<RSSChannel> = _channel

    private val _article = MutableLiveData<RssFeed>()
    val article: LiveData<RssFeed> = _article

    init {
        // trigger userCase here in a coroutine
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            _channel.postValue(
                useCase.getFeeds(url)
            )

        }
    }


    companion object {
        const val TAG = "FeedsViewModel"

        @Suppress("Unchecked_Cast")
        class FeedsViewModelFactory constructor(
            private val useCase: GetFeedsUseCase,
            private val url: String
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return if (modelClass.isAssignableFrom(FeedsViewModel::class.java)) {
                    FeedsViewModel(useCase, url) as T
                } else {
                    throw IllegalArgumentException("FeedsViewModel Not Found")
                }
            }
        }
    }


}