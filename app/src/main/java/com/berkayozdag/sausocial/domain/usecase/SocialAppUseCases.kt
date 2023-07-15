package com.berkayozdag.sausocial.domain.usecase

data class SocialAppUseCases(
    val getPosts: GetPosts,
    val getPostById: GetPostById,
    val sendPost: SendPost,
    val sendComment: SendComment,
    val getUserById: GetUserById,
    val getUsers: GetUsers,
    val follow: Follow,
    val unFollow: UnFollow,
    val postLike: PostLike,
    val postDisLike: PostDisLike,
    val getFollowingPosts: GetFollowingPosts,
    val postDelete: PostDelete,
)