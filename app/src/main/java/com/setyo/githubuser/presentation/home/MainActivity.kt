package com.setyo.githubuser.presentation.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyo.githubuser.R
import com.setyo.githubuser.core.data.Resource
import com.setyo.githubuser.core.ui.ListUsersAdapter
import com.setyo.githubuser.databinding.ActivityMainBinding
import com.setyo.githubuser.presentation.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListUsersAdapter
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerView()
        showData(getString(R.string.id_username))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showData(query.toString())
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, Class.forName(
                    "com.setyo.githubuser.favorite.presentation.FavoriteUserActivity"
                    )
                )
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showData(username: String) {
        mainViewModel.getListUser(username).observe(this) { users ->
            users?.let {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showEmpty(false)
                        showError(false)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (users.data.isNullOrEmpty()) {
                            showEmpty(true)
                        } else {
                            showEmpty(false)
                            showToast(username)
                            adapter.submitList(users.data)
                            adapter.onItemClick = { selectedData ->
                                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_USER, selectedData.login)
                                startActivity(intent)
                            }
                        }
                        showError(false)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showEmpty(false)
                        showError(true)
                    }
                }
            }
        }
    }

    private fun showRecyclerView() {
        adapter = ListUsersAdapter()

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun showEmpty(state: Boolean) {
        binding.tvState.visibility = if (state) View.VISIBLE else View.GONE
        binding.tvState.text = getString(R.string.empty_data)
    }

    private fun showError(state: Boolean) {
        binding.tvState.visibility = if (state) View.VISIBLE else View.GONE
        binding.tvState.text = getString(R.string.error)
    }


    companion object {
        fun start(context: Context) {
            Intent(context, MainActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}