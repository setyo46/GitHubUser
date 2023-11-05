package com.setyo.githubuser.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.setyo.githubuser.R
import com.setyo.githubuser.core.data.Resource
import com.setyo.githubuser.core.domain.model.User
import com.setyo.githubuser.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }

        val username = intent.getStringExtra(EXTRA_USER).toString()
        getDetailUser(username)
        initPager(username)
    }

    private fun initPager(username: String) {
        val sectionPagerAdapter = SelectionsPagerAdapter( this@DetailActivity, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tab: TabLayout = binding.tabLayout
        TabLayoutMediator(tab, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun getDetailUser(username: String) {
        detailViewModel.getDetailUser(username).observe(this@DetailActivity) { user ->
            if(user != null) {
                when(user) {
                    is Resource.Loading -> {
                        showLoading(true)
                        stateEmpty(false)
                        stateError(false)
                    }
                    is Resource.Success -> {
                        if(user.data != null) {
                            stateEmpty(false)
                            showLoading(false)
                            binding.apply {
                                Glide.with(this@DetailActivity)
                                    .load(user.data!!.avatarUrl)
                                    .into(ivAvatar)

                                tvUsername.text = user.data!!.login
                                tvName.text = user.data!!.name
                                tabLayout.getTabAt(0)?.text = "${user.data!!.followers} Followers"
                                tabLayout.getTabAt(1)?.text = "${user.data!!.following} Following"
                                setFavoriteUser(user.data!!)
                            }
                            stateError(false)
                        } else {
                            stateEmpty(true)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        stateEmpty(false)
                        stateError(true)
                    }
                }
            }
        }
    }

    private fun setFavoriteUser(user: User) {
        detailViewModel.getFavoriteUserByUsername(user.login).observe(this) { favoriteUsername  ->
            if (favoriteUsername) {
                    binding.fabAdd.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding.fabAdd.setImageDrawable(
                        ContextCompat.getDrawable(
                        this,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }

                binding.fabAdd.setOnClickListener {
                    if (favoriteUsername) {
                        showToast(getString(R.string.delete_fav))
                        detailViewModel.deleteUser(user)
                    } else {
                        showToast(getString(R.string.add_fav))
                        detailViewModel.insertUser(user)
                    }
                }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun stateEmpty(isEmpty: Boolean) {
        if(isEmpty) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(R.string.empty_data)
    }

    private fun stateError(isError: Boolean) {
        if(isError) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(R.string.error)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

}