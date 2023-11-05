package com.setyo.githubuser.favorite.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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
            title = getString(com.setyo.githubuser.R.string.title_favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        loadKoinModules(favoriteModule)

        showRecyclerView()
        showFavoriteData()
    }

    private fun showFavoriteData() {
        favoriteUserViewModel.getAllFavoriteUser().observe(this) { users ->
            if (users.isNullOrEmpty()) {
                adapter.submitList(null)
                stateEmpty(true)
                adapter.submitList(null)
            } else {
                stateEmpty(false)
                adapter.submitList(users)
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

        binding.apply {
            rvGithubFavorite.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvGithubFavorite.setHasFixedSize(true)
            rvGithubFavorite.adapter = adapter
        }
    }

    private fun stateEmpty(isEmpty: Boolean) {
        if(isEmpty) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(com.setyo.githubuser.R.string.empty_data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

}




