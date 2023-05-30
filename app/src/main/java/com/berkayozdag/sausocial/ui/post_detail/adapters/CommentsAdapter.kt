package com.berkayozdag.sausocial.ui.post_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.databinding.ItemCommentBinding
import com.berkayozdag.sausocial.model.Comment

class CommentsAdapter() :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var items: List<Comment> = emptyList()

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            textViewUserName.text = comment.appUser.name + " " + comment.appUser.surname
            textViewUserDepartment.text = comment.appUser.part
            textViewComment.text = comment.message
            textViewCommentCreatedDate.text = comment.publishedDate
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