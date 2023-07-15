package com.berkayozdag.sausocial.ui.groups.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.databinding.ItemGroupBinding

class GroupsAdapter(var onItemClicked: ((Int) -> Unit) = {}) :
    RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    var items: List<ProfileResponse> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profileResponse: ProfileResponse) = with(binding) {
            profileResponse.profileImageUrl.let {
                imageViewGroup.loadImage(profileResponse.profileImageUrl)
            }
            textViewMembersCount.text = profileResponse.followers.size.toString()
            textViewGroupName.text = "${profileResponse.name} ${profileResponse.surname}"
            imageViewGroup.setOnClickListener {
                onItemClicked(profileResponse.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}