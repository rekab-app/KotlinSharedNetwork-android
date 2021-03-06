package com.sixthsolution.rekab.sharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import io.ktor.client.HttpClient
import io.ktor.client.features.ExpectSuccess
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlin.coroutines.CoroutineContext

internal expect val dispatcher: CoroutineDispatcher

class ApiClient: CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(Post::class, Post.serializer())
            }
        }
        install(ExpectSuccess)
    }

    fun getPosts(successCallback: (List<Post>) -> Unit, errorCallback: (Exception) -> Unit) {
        launch {
            try {
                val result: String = httpClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        port = 443
                        host = "jsonplaceholder.typicode.com"
                        encodedPath = "/posts"
                    }
                }

                val repos = Json.parse(Post.serializer().list, result)
                successCallback(repos)
            } catch (ex: Exception) {
                errorCallback(ex)
            }
        }
    }

    fun dispose() {
        job.cancel()
    }
}

@Serializable
data class Post(val userId: Int, val id: Int, val title: String, val body: String)