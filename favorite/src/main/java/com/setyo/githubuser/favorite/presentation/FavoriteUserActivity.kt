package com.setyo.githubuser.favorite.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyo.githubuser.R
import com.setyo.githubuser.core.ui.ListUsersAdapter
import com.setyo.githubuser.favorite.databinding.ActivityFavoriteUserBinding
import com.setyo.githubuser.favorite.di.favoriteModule
import com.setyo.githubuser.presentation.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: ListUsersAdapter
    private val favoriteUserViewModel: FavoriteUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.title_favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        loadKoinModules(favoriteModule)

        showRecyclerView()
        showFavoriteData()
    }

    private fun showFavoriteData() {
        favoriteUserViewModel.getAllFavoriteUser().observe(this) {
            if (it.isNullOrEmpty()) {
                adapter.submitList(null)
                showEmpty(true)
                adapter.submitList(null)
            } else {
                showEmpty(false)
                adapter.submitList(it)
                adapter.onItemClick = { selectedData ->
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, selectedData.login)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showRecyclerView() {
        adapter= ListUsersAdapter()

        binding.rvGithubFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            setHasFixedSize(true)
            adapter = this@FavoriteUserActivity.adapter
        }
    }

    private fun showEmpty(state: Boolean) {
        binding.tvState.visibility = if (state) View.VISIBLE else View.GONE
        binding.tvState.text = getString(R.string.empty_data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

}




