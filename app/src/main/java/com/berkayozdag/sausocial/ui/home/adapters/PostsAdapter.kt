package com.berkayozdag.sausocial.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.databinding.ItemPostBinding
import com.berkayozdag.sausocial.model.Post

class PostsAdapter(
    var postItemClicked: ((Int) -> Unit) = {},
    var userItemClicked: ((Int) -> Unit) = {}
) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var items: List<Post> = emptyList()

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) = with(binding) {
            textViewUserName.text = post.appUser.name + " " + post.appUser.surname
            textViewUserDepartment.text = post.appUser.part
            textViewPostDescription.text = post.content
            textViewPostCreatedDate.text = post.publishedDate
            textViewPostNumberOfComment.text = post.comments.size.toString()
            textViewPostNumberOfLike.text = post.likeCount.toString()
            layoutPost.setOnClickListener {
                postItemClicked(post.id)
            }
            imageViewUserProfile.setOnClickListener {
                userItemClicked(post.appUser.id)
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