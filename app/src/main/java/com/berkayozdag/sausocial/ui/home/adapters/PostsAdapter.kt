package com.berkayozdag.sausocial.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.util.Constants
import com.berkayozdag.sausocial.common.util.GenericDiffUtil
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.databinding.ItemPostBinding

class PostsAdapter(
    var postItemClicked: (Int) -> Unit = {},
    var userItemClicked: (Int) -> Unit = {},
    var likeClicked: (Int, Int) -> Unit = { _, _ -> },
    var disLikeClicked: (Int, Int) -> Unit = { _, _ -> },
    var postDelete: (Int) -> Unit = {}
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var items: List<Post> = emptyList()
    var appUserId: Int = -1
    var isProfile: Boolean = false

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                post.appUser.profileImageUrl.let { url->
                    imageViewUserProfile.loadImage(Constants.API_USER_IMAGES_URL + url)
                }
                textViewUserName.text = "${post.appUser.name} ${post.appUser.surname}"
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
                buttonPostLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (post.isUserLikedThisPost(appUserId)) R.drawable.ic_liked else R.drawable.ic_like
                    )
                )
                buttonPostDelete.setVisible(isProfile)
                textViewUserDepartment.setVisible(!post.appUser.part.isNullOrEmpty())

                buttonPostLike.setOnClickListener {
                    if (post.isUserLikedThisPost(appUserId)) disLikeClicked(post.id, appUserId)
                    else likeClicked(post.id, appUserId)
                }

                buttonPostDelete.setOnClickListener {
                    postDelete(post.id)
                }
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

    fun setData(newPostItemEntities: List<Post>) {
        val diffCallback = GenericDiffUtil(items, newPostItemEntities)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newPostItemEntities
        diffResult.dispatchUpdatesTo(this)
    }

}