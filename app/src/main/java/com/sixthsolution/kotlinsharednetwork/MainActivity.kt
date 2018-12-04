package com.sixthsolution.kotlinsharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.sixthsolution.rekab.sharednetwork.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadPosts()
    }

    private fun loadPosts() {
        val client = ApiClient()
        client.getPosts(successCallback = {
            GlobalScope.launch(Dispatchers.Main) {
                recycler.setHasFixedSize(false)
                val adapter = PostAdapter(it)
                val layoutManager = LinearLayoutManager(this@MainActivity)
                recycler.layoutManager = layoutManager
                recycler.adapter = adapter
            }
        }, errorCallback = {
            GlobalScope.launch(Dispatchers.Main) {
                Log.e("Get Post Error", it.message)
            }
        })
    }
}
