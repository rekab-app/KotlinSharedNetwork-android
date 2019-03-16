package com.sixthsolution.kotlinsharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.sixthsolution.rekab.sharednetwork.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var client: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()
        client = ApiClient()
        loadPosts()
    }

    private fun loadPosts() {
        client.getPosts(successCallback = {
            launch {
                recycler.setHasFixedSize(false)
                val adapter = PostAdapter(it)
                val layoutManager = LinearLayoutManager(this@MainActivity)
                recycler.layoutManager = layoutManager
                recycler.adapter = adapter
            }
        }, errorCallback = {
            launch {
                Log.e("Get Post Error", it.message)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        job.cancel()
        client.dispose()
    }
}
