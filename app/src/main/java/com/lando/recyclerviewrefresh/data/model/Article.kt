package com.lando.recyclerviewrefresh.data.model

data class Article(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val commentsCount: Long,
    val bookmarked: Boolean
)
