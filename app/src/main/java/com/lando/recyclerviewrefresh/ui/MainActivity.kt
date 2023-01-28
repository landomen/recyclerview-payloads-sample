package com.lando.recyclerviewrefresh.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
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

            R.id.action_reorder -> {
                viewModel.onUserAction(MainViewModel.Action.ReorderClicked)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
//        adapter = ArticlesRecyclerViewAdapterWithoutPayload { articleId ->
//            viewModel.onUserAction(MainViewModel.Action.ArticleBookmarkClicked(articleId.id))
//        }
        adapter = ArticlesRecyclerViewAdapterWithPayload { articleId ->
            viewModel.onUserAction(MainViewModel.Action.ArticleBookmarkClicked(articleId.id))
        }
        binding.rvArticles.adapter = adapter
        binding.rvArticles.setHasFixedSize(true)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                // remain scrolled to top for demo purposes
                binding.rvArticles.scrollToPosition(0)
            }
        })
    }

    private fun bindStateUpdates() {
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                adapter.submitList(state.articles)
            }
        }
    }
}
