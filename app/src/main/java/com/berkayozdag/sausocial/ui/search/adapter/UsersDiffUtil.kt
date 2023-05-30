package com.berkayozdag.sausocial.ui.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.berkayozdag.sausocial.model.profile.ProfileResponse

class UsersDiffUtil(
    private val oldList: List<ProfileResponse>,
    private val newList: List<ProfileResponse>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id != newList[newItemPosition].id
    }
}