package com.lando.recyclerviewrefresh.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lando.recyclerviewrefresh.R
import com.lando.recyclerviewrefresh.databinding.ActivityMainBinding
import com.lando.recyclerviewrefresh.ui.adapter.ArticlesRecyclerViewAdapterWithPayload
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticlesRecyclerViewAdapterWithPayload // ArticlesRecyclerViewAdapterWithoutPayload

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        bindStateUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.onUserAction(MainViewModel.Action.RefreshClicked)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
//        adapter = ArticlesRecyclerViewAdapterWithoutPayload { articleId ->
//            viewModel.bookmarkArticle(articleId.id)
//        }
        adapter = ArticlesRecyclerViewAdapterWithPayload { articleId ->
            viewModel.onUserAction(MainViewModel.Action.ArticleBookmarkClicked(articleId.id))
        }
        binding.rvArticles.adapter = adapter
        binding.rvArticles.setHasFixedSize(true)
    }

    private fun bindStateUpdates() {
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                adapter.submitList(state.articles)
            }
        }
    }
}
