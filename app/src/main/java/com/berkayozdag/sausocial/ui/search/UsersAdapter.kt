package com.berkayozdag.sausocial.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Post
import com.berkayozdag.sausocial.common.User
import com.berkayozdag.sausocial.databinding.ItemPostBinding
import com.berkayozdag.sausocial.databinding.ItemUserBinding

class UsersAdapter(var onItemClicked: ((User) -> Unit) = {}) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private var items: List<User> = emptyList()

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            textViewUserName.text = user.userName
            textViewUserDepartment.text = user.userDepartment
            layoutPost.setOnClickListener {
                onItemClicked(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newUserItems: List<User>) {
        val diffUtil = UsersDiffUtil(items, newUserItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newUserItems
        diffResults.dispatchUpdatesTo(this)
    }

}