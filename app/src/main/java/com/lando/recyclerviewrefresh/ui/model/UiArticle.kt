package com.lando.recyclerviewrefresh.ui.model

data class UiArticle(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val commentsCount: Long,
    val bookmarked: Boolean
)
