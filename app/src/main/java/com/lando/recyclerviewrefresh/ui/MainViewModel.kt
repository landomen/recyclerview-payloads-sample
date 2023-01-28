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
class MainViewModel @Inject constructor(private val repository: DummyArticleRepository) :
    ViewModel() {

    val stateFlow: Flow<State> =
        repository.articlesFlow.map { articles -> articles.map { it.mapToUiArticle() } }
            .map { articles -> State(articles) }

    fun onUserAction(action: Action) {
        when (action) {
            is Action.ArticleBookmarkClicked -> bookmarkArticle(action.articleId)
            Action.RefreshClicked -> refreshArticles()
            Action.ReorderClicked -> reorderArticles()
        }
    }

    private fun refreshArticles() {
        viewModelScope.launch {
            repository.refreshArticles()
        }
    }

    private fun reorderArticles() {
        viewModelScope.launch {
            repository.reorderArticles()
        }
    }

    private fun bookmarkArticle(articleId: String) {
        viewModelScope.launch {
            repository.bookmarkArticle(articleId)
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

    data class State(val articles: List<UiArticle>)

    sealed interface Action {
        object RefreshClicked : Action

        object ReorderClicked : Action

        data class ArticleBookmarkClicked(val articleId: String) : Action
    }
}
