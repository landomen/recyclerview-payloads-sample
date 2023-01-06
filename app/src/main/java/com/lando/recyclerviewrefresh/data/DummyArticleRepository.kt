package com.lando.recyclerviewrefresh.data

import com.lando.recyclerviewrefresh.data.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object DummyArticleRepository {

    private const val COMMENTS_COUNT_MIN = 4L
    private const val COMMENTS_COUNT_MAX = 100L

    private val originalArticles = listOf(
        generateArticle(
            id = "1",
            title = "New version of Kotlin released",
            subtitle = "KOTLIN 1.8",
            imageUrl = "https://kotlinlang.org/assets/images/twitter/general.png",
            bookmarked = true
        ),
        generateArticle(
            id = "2",
            title = "How to get started with Jetpack Compose",
            subtitle = "JETPACK COMPOSE",
            imageUrl = "https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1472&q=80",
            bookmarked = false
        ),
        generateArticle(
            id = "3",
            title = "New version of Android Studio released",
            subtitle = "DOLPHIN",
            imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
            bookmarked = true
        ),
        generateArticle(
            id = "4",
            title = "Writing an implementation plan before implementing a new feature",
            subtitle = "DOCUMENTATION",
            imageUrl = "https://images.unsplash.com/photo-1501504905252-473c47e087f8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1548&q=80",
            bookmarked = false
        )
    )

    private val articlesMutableFlow = MutableStateFlow(originalArticles)
    val articlesFlow = articlesMutableFlow.asStateFlow()

    suspend fun refreshArticles() {
        val updatedArticles = articlesFlow.value.map {
            it.copy(
                commentsCount = it.commentsCount + generateCommentsCount()
            )
        }
        articlesMutableFlow.emit(updatedArticles)
    }

    suspend fun bookmarkArticle(articleId: String) {
        val updatedArticles = articlesFlow.value.map {
            if (it.id == articleId) {
                it.copy(bookmarked = !it.bookmarked)
            } else {
                it
            }
        }
        articlesMutableFlow.emit(updatedArticles)
    }

    private fun generateArticle(
        id: String,
        title: String,
        subtitle: String,
        imageUrl: String,
        bookmarked: Boolean
    ): Article {
        return Article(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            commentsCount = generateCommentsCount(),
            bookmarked = bookmarked
        )
    }

    private fun generateCommentsCount() = Random.nextLong(COMMENTS_COUNT_MIN, COMMENTS_COUNT_MAX)
}
