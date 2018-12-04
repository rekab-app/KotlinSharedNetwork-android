package com.sixthsolution.kotlinsharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sixthsolution.rekab.sharednetwork.Post

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return posts.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = posts[position].title
        holder.body.text = posts[position].body
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        val title: TextView
        val body: TextView

        constructor(itemView: View) : super(itemView) {
            title = itemView.findViewById(R.id.title)
            body = itemView.findViewById(R.id.body)
        }
    }
}