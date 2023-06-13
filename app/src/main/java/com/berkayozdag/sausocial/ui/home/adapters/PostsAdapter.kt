package com.berkayozdag.sausocial.ui.home.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.databinding.ItemPostBinding
import com.berkayozdag.sausocial.model.Post
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class PostsAdapter(
    var postItemClicked: ((Int) -> Unit) = {},
    var userItemClicked: ((Int) -> Unit) = {},
    var likeClicked: ((Int, Int) -> Unit) = { _, _ -> },
    var disLikeClicked: ((Int, Int) -> Unit) = { _, _ -> },
    var postDelete: ((Int) -> Unit) = {},
) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var items: List<Post> = emptyList()
    var appUserId: Int = -1
    var isProfile: Boolean = false

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) = with(binding) {

            Glide.with(itemView.context)
                .load("https://sausocialmedia.com.tr/api/User/Images/${post.appUser.profileImageUrl}")
                .into(imageViewUserProfile)
            textViewUserName.text = post.appUser.name + " " + post.appUser.surname
            textViewUserDepartment.text = post.appUser.part
            textViewPostDescription.text = post.content
            textViewPostCreatedDate.text = post.publishedDate
            textViewPostNumberOfComment.text = post.comments.size.toString()
            textViewPostNumberOfLike.text = post.likes.size.toString()
            layoutPost.setOnClickListener {
                postItemClicked(post.id)
            }
            imageViewUserProfile.setOnClickListener {
                userItemClicked(post.appUser.id)
            }
            if (post.isUserLikedThisPost(appUserId)) buttonPostLike.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_liked
                )
            )
            else buttonPostLike.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_like
                )
            )

            if (isProfile) postDeleteButton.setVisible(true)
            else postDeleteButton.setVisible(false)

            buttonPostLike.setOnClickListener {
                if (post.isUserLikedThisPost(appUserId)) disLikeClicked(post.id, appUserId)
                else likeClicked(post.id, appUserId)
            }

            postDeleteButton.setOnClickListener {
                postDelete(post.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newPostItems: List<Post>) {
        val diffUtil = PostsDiffUtil(items, newPostItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newPostItems
        diffResults.dispatchUpdatesTo(this)
    }
}