package com.harisewak.rssreader.presentation.feeds

import androidx.lifecycle.*
import com.harisewak.rssreader.domain.usecase.GetFeedsUseCase
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedsViewModel(
    private val useCase: GetFeedsUseCase,
    private val url: String
) : ViewModel() {

    fun setCurrentArticle(article: Article) {
        _article.value = article
    }

    private val _channel = MutableLiveData<Channel>()
    val channel: LiveData<Channel> = _channel

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

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