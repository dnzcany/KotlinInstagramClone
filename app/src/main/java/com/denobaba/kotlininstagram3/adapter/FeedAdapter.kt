package com.denobaba.kotlininstagram3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denobaba.kotlininstagram3.FeedActivity
import com.denobaba.kotlininstagram3.Post
import com.denobaba.kotlininstagram3.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class FeedAdapter(private val PostList :ArrayList<Post>): RecyclerView.Adapter<FeedAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)

    }

    override fun getItemCount(): Int {
        return PostList.size

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.emailrecycle.text = PostList.get(position).Email
        holder.binding.commentrecycle.text = PostList.get(position).comment
        Picasso.get().load(PostList.get(position).downloadUrl).into(holder.binding.photorecycle)


    }

}