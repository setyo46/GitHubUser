package com.setyo.githubuser.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyo.githubuser.R
import com.setyo.githubuser.core.data.Resource
import com.setyo.githubuser.core.domain.model.User
import com.setyo.githubuser.core.ui.ListUsersAdapter
import com.setyo.githubuser.databinding.FragmentFollowBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var adapter: ListUsersAdapter
    private val followViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        showRecyclerView()

         var position = 0
         var username = "test"

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
            showToast(username)
        }


        if (position == 1) {
           followViewModel.getFollowersUser(username).observe(viewLifecycleOwner) {
                showData(it)
           }
        } else {
            followViewModel.getFollowingUser(username).observe(viewLifecycleOwner) {
                showData(it)
            }
        }
    }

    private fun showData(user: Resource<List<User>>?) {
            if (user != null) {
                when (user) {
                    is Resource.Loading -> {
                        showLoading(true)
                        stateEmpty(false)
                        stateError(false)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (user.data.isNullOrEmpty()) {
                            stateEmpty(true)
                        } else {
                            stateEmpty(false)
                            adapter.submitList(user.data)
                            adapter.onItemClick = { selectedData ->
                                val intent = Intent(requireActivity(), DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_USER, selectedData.login)
                                startActivity(intent)
                            }
                            stateError(false)
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


    private fun showRecyclerView() {
        adapter = ListUsersAdapter()

        binding.apply {
            rvFollowerFollowing.layoutManager = LinearLayoutManager(requireContext())
            rvFollowerFollowing.setHasFixedSize(true)
            rvFollowerFollowing.adapter = adapter
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

    private fun showLoading(state: Boolean) { binding.progressBar.visibility =
        if (state) View.VISIBLE else View.GONE }

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}