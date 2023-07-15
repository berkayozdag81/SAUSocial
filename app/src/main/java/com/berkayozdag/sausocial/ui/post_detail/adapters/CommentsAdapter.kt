package com.berkayozdag.sausocial.ui.post_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.util.Constants
import com.berkayozdag.sausocial.common.util.GenericDiffUtil
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.data.entities.Comment
import com.berkayozdag.sausocial.databinding.ItemCommentBinding

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

    fun setData(newCommentItemEntities: List<Comment>) {
        val diffCallback = GenericDiffUtil(items, newCommentItemEntities)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newCommentItemEntities
        diffResult.dispatchUpdatesTo(this)
    }

}