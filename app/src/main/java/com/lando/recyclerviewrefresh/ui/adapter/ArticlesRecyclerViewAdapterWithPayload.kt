package com.lando.recyclerviewrefresh.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lando.recyclerviewrefresh.R
import com.lando.recyclerviewrefresh.databinding.ItemArticleBinding
import com.lando.recyclerviewrefresh.ui.model.UiArticle

internal class ArticlesRecyclerViewAdapterWithPayload(private val onArticleBookmarkClicked: ArticleBookmarkClickListener) :
    ListAdapter<UiArticle, ArticlesRecyclerViewAdapterWithPayload.ArticleViewHolder>(
        ArticleDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        Log.e("NEKI", "regular onBindViewHolder")
        holder.bind(getItem(position)) {
            onArticleBookmarkClicked(ArticleId(it.id))
        }
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        Log.e("NEKI", "withPayloads onBindViewHolder")
        Log.e("NEKI", "payloads: $payloads")


        when (val latestPayload = payloads.lastOrNull()) {
            is ArticleChangePayload.Comments -> holder.bindCommentsCount(latestPayload.newCommentsCount)
            is ArticleChangePayload.Bookmark -> holder.bindBookmarkState(latestPayload.bookmarked)
            else -> onBindViewHolder(holder, position)
        }
    }

    internal class ArticleViewHolder(private val itemBinding: ItemArticleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(article: UiArticle, onBookmarkClicked: (UiArticle) -> Unit) {
            bindTitle(article.title)
            bindSubtitle(article.subtitle)
            bindCommentsCount(article.commentsCount)
            bindBookmarkButton(article.bookmarked) {
                onBookmarkClicked(article)
            }
            bindImage(article.imageUrl)
        }

        private fun bindTitle(title: String) {
            itemBinding.tvTitle.text = title
        }

        private fun bindSubtitle(subtitle: String) {
            itemBinding.tvSubtitle.text = subtitle
        }

        internal fun bindCommentsCount(commentsCount: Long) {
            itemBinding.tvCommentCount.text = commentsCount.toString()
        }

        private fun bindBookmarkButton(bookmarked: Boolean, onBookmarkClicked: () -> Unit) {
            bindBookmarkState(bookmarked)
            itemBinding.ibBookmark.setOnClickListener {
                onBookmarkClicked()
            }
        }

        internal fun bindBookmarkState(bookmarked: Boolean) {
            itemBinding.ibBookmark.setImageResource(
                if (bookmarked) {
                    R.drawable.ic_bookmark_active
                } else {
                    R.drawable.ic_bookmark_inactive
                }
            )
        }

        private fun bindImage(imageUrl: String) {
            Glide.with(itemBinding.ivArticle.context)
                .load(imageUrl)
                .centerCrop()
                .into(itemBinding.ivArticle)
        }
    }

    private class ArticleDiffCallback : DiffUtil.ItemCallback<UiArticle>() {

        override fun areItemsTheSame(oldItem: UiArticle, newItem: UiArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiArticle, newItem: UiArticle): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: UiArticle, newItem: UiArticle): Any? {
            return when {
                oldItem.commentsCount != newItem.commentsCount -> {
                    ArticleChangePayload.Comments(newItem.commentsCount)
                }

                oldItem.bookmarked != newItem.bookmarked -> {
                    ArticleChangePayload.Bookmark(newItem.bookmarked)
                }

                else -> super.getChangePayload(oldItem, newItem)
            }
        }
    }

    private sealed interface ArticleChangePayload {

        data class Comments(val newCommentsCount: Long) : ArticleChangePayload

        data class Bookmark(val bookmarked: Boolean) : ArticleChangePayload

    }
}

