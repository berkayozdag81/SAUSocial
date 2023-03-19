package com.berkayozdag.sausocial.ui.post_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Comment
import com.berkayozdag.sausocial.databinding.ItemCommentBinding

class CommentsAdapter(var onItemClicked: ((Comment) -> Unit) = {}) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var items: List<Comment> = emptyList()

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            textViewUserName.text = comment.user.userName
            textViewUserDepartment.text = comment.user.userDepartment
            textViewComment.text = comment.comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newCommentItems: List<Comment>) {
        val diffUtil = CommentsDiffUtil(items, newCommentItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newCommentItems
        diffResults.dispatchUpdatesTo(this)
    }

}