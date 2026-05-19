package com.example.closetstack

object PostRepository {
    private val userPosts = mutableListOf<Post>()
    var posts: List<Post> = emptyList()

    fun addPost(post: Post) {
        userPosts.add(0, post)
        posts = userPosts + posts.filter { !userPosts.contains(it) }
    }

    fun getUserPosts(): List<Post> = userPosts.toList()
}