package com.berkayozdag.sausocial.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Post
import com.berkayozdag.sausocial.databinding.ItemPostBinding

class PostsAdapter(var onItemClicked: ((Post) -> Unit) = {}) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var items: List<Post> = emptyList()

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) = with(binding) {
            textViewUserName.text = post.user.userName
            textViewUserDepartment.text = post.user.userDepartment
            textViewPostDescription.text = post.description
            textViewPostCreatedDate.text = post.createdDate
            textViewPostNumberOfComment.text = post.numberOfComment.toString()
            textViewPostNumberOfLike.text = post.numberOfLike.toString()
            post.postImage?.let {
                binding.imageViewPost.visibility = View.VISIBLE
            }
            layoutPost.setOnClickListener {
                onItemClicked(post)
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