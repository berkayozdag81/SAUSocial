package com.berkayozdag.sausocial.ui.groups.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Group
import com.berkayozdag.sausocial.databinding.ItemGroupBinding
import com.bumptech.glide.Glide

class GroupsAdapter(var onItemClicked: ((Group) -> Unit) = {}) :
    RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    var items: List<Group> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) = with(binding) {
               textViewGroupName.text=group.name
                Glide
                .with(binding.root)
                .load(group.imageUrl)
                .centerCrop()
                .into(imageViewGroup)
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