package com.berkayozdag.sausocial.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.util.GenericDiffUtil
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.databinding.ItemUserBinding

class UsersAdapter(var onItemClicked: ((ProfileResponse) -> Unit) = {}) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private var items: List<ProfileResponse> = emptyList()

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ProfileResponse) = with(binding) {
            user.profileImageUrl.let {
                imageViewUserProfile.loadImage(user.profileImageUrl)
            }
            textViewUserName.text = "${user.name} ${user.surname}"
            textViewUserDepartment.text = user.part
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

    fun setData(newUserItems: List<ProfileResponse>) {
        val diffCallback = GenericDiffUtil(items, newUserItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newUserItems
        diffResult.dispatchUpdatesTo(this)
    }

}