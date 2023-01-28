package com.lando.recyclerviewrefresh.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lando.recyclerviewrefresh.databinding.ItemArticleBinding
import com.lando.recyclerviewrefresh.ui.model.UiArticle

@JvmInline
internal value class ArticleId(val id: String)
internal typealias ArticleBookmarkClickListener = (ArticleId) -> Unit

internal class ArticlesRecyclerViewAdapterWithoutPayload(private val onArticleBookmarkClicked: ArticleBookmarkClickListener) :
    ListAdapter<UiArticle, ArticlesRecyclerViewAdapterWithoutPayload.ArticleViewHolder>(
        ArticleDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            onArticleBookmarkClicked(ArticleId(it.id))
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

        private fun bindCommentsCount(commentsCount: Long) {
            itemBinding.tvCommentCount.text = commentsCount.toString()
        }

        private fun bindBookmarkButton(bookmarked: Boolean, onBookmarkClicked: () -> Unit) {
            itemBinding.ibBookmark.isSelected = bookmarked
            itemBinding.ibBookmark.setOnClickListener {
                onBookmarkClicked()
            }
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
    }
}
