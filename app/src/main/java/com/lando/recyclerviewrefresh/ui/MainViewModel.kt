package com.lando.recyclerviewrefresh.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lando.recyclerviewrefresh.data.DummyArticleRepository
import com.lando.recyclerviewrefresh.data.model.Article
import com.lando.recyclerviewrefresh.ui.model.UiArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val articlesFlow: Flow<List<UiArticle>> = DummyArticleRepository.articlesFlow
        .map { articles -> articles.map { it.mapToUiArticle() } }

    fun refreshArticles() {
        viewModelScope.launch {
            DummyArticleRepository.refreshArticles()
        }
    }

    fun bookmarkArticle(articleId: String) {
        viewModelScope.launch {
            DummyArticleRepository.bookmarkArticle(articleId)
        }
    }

    private fun Article.mapToUiArticle(): UiArticle {
        return UiArticle(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            commentsCount = commentsCount,
            bookmarked = bookmarked
        )
    }
}
