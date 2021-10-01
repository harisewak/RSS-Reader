package com.harisewak.rssreader.presentation.feeds

import androidx.lifecycle.*
import com.harisewak.rssreader.common.Constants
import com.harisewak.rssreader.common.Result
import com.harisewak.rssreader.data.model.RssFeed
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedsViewModel(
    private val useCase: GetFeedsUseCase
) : ViewModel() {
    var url: String = Constants.URL_FEED_BACKCHANNEL

    fun setCurrentArticle(article: RssFeed) {
        _article.value = article
    }

    suspend fun updateBookmarkStatus(guid: String, isBookmarked: Boolean) {
        useCase.updateBookmarkStatus(guid, isBookmarked)
    }

    private val _channel = MutableLiveData<Result>()
    val channel: LiveData<Result> = _channel

    private val _article = MutableLiveData<RssFeed>()
    val article: LiveData<RssFeed> = _article

    init {
        getFeeds()
    }

    fun getFeeds() {
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
                    FeedsViewModel(useCase) as T
                } else {
                    throw IllegalArgumentException("FeedsViewModel Not Found")
                }
            }
        }
    }


}