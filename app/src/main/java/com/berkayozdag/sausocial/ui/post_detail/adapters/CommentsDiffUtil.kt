package com.berkayozdag.sausocial.ui.post_detail.adapters

import androidx.recyclerview.widget.DiffUtil
import com.berkayozdag.sausocial.model.Comment

class CommentsDiffUtil(
    private val oldList: List<Comment>,
    private val newList: List<Comment>
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