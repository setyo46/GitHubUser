package com.setyo.githubuser.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
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
        showPagerForUser(username)
    }

    private fun getDetailUser(username: String) {
        detailViewModel.getDetailUser(username).observe(this@DetailActivity) { user ->
            user?.let {
                when(it) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showEmpty(false)
                        showError(false)
                    }

                    is Resource.Success -> {
                        val userData = it.data
                        if(userData != null) {
                            binding.apply {
                                tvName.text = userData.name
                                tvUsername.text = userData.login
                                Glide.with(applicationContext)
                                    .load(userData.avatarUrl)
                                    .into(ivAvatar)
                                tabLayout.getTabAt(0)?.text = "${userData.followers} Followers"
                                tabLayout.getTabAt(1)?.text = "${userData.following} Following"
                                setFavoriteUser(userData)
                            }
                            showEmpty(false)
                            showLoading(false)
                            showError(false)
                        } else {
                            showEmpty(true)
                        }
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

    private fun setFavoriteUser(user: User) {
        detailViewModel.getFavoriteUserByUsername(user.login).observe(this) {
            if (it) {
                binding.fabAdd.setImageResource(R.drawable.baseline_favorite_24)
                binding.fabAdd.setOnClickListener {
                    detailViewModel.deleteUser(user)
                    showToast(getString(R.string.delete_fav))
                }
            } else {
                binding.fabAdd.setImageResource(R.drawable.baseline_favorite_border_24)
                binding.fabAdd.setOnClickListener {
                    detailViewModel.insertUser(user)
                    showToast(getString(R.string.add_fav))
                }
            }
        }
    }

    private fun showPagerForUser(username: String) {
        val sectionPagerAdapter = SelectionsPagerAdapter(this@DetailActivity, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
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