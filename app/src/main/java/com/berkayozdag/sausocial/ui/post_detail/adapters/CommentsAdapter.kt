package com.berkayozdag.sausocial.ui.post_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Constants
import com.berkayozdag.sausocial.common.loadImage
import com.berkayozdag.sausocial.databinding.ItemCommentBinding
import com.berkayozdag.sausocial.model.Comment

class CommentsAdapter(var userItemClicked: ((Int) -> Unit) = {}) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var items: List<Comment> = emptyList()

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            comment.appUser.profileImageUrl.let {
                imageViewUserProfile.loadImage(Constants.API_USER_IMAGES_URL + comment.appUser.profileImageUrl)
            }
            textViewUserName.text = "${comment.appUser.name} ${comment.appUser.surname}"
            textViewUserDepartment.text = comment.appUser.part
            textViewComment.text = comment.message
            textViewCommentCreatedDate.text = comment.publishedDate
            imageViewUserProfile.setOnClickListener {
                userItemClicked(comment.appUser.id)
            }
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