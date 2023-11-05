package com.setyo.githubuser.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.setyo.githubuser.core.R
import com.setyo.githubuser.core.databinding.ItemRowUserBinding
import com.setyo.githubuser.core.domain.model.User

class ListUsersAdapter : ListAdapter<User, ListUsersAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((User) -> Unit)? = null

    inner class ListViewHolder(private val binding: ItemRowUserBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvUsername.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .error(R.drawable.baseline_person_24)
                    .into(ivItemAvatar)
            }
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedUser = getItem(position)
                    onItemClick?.invoke(clickedUser)
                }

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ListViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null ) {
            viewHolder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.login == newItem.login
            }

        }
    }
}