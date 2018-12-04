package com.sixthsolution.rekab.sharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import io.ktor.client.HttpClient
import io.ktor.client.features.ExpectSuccess
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonTreeParser
import kotlinx.serialization.json.content
import kotlinx.serialization.json.int

internal expect val dispatcher: CoroutineDispatcher

class ApiClient() {
    private val httpClient = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(Post::class, Post.serializer())
            }
        }
        install(ExpectSuccess)
    }

    fun getPosts(successCallback: (List<Post>) -> Unit, errorCallback: (Exception) -> Unit) {
        GlobalScope.launch(dispatcher) {
            try {
                val result: String = httpClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        port = 443
                        host = "jsonplaceholder.typicode.com"
                        encodedPath = "/posts"
                    }
                }

                val repos = JsonTreeParser(result).read().jsonArray
                        .map { it.jsonObject }
                        .map { Post(it["userId"].int, it["id"].int, it["title"].content, it["body"].content) }
                successCallback(repos)
            } catch (ex: Exception) {
                errorCallback(ex)
            }
        }
    }
}

@Serializable
data class Post(val userId: Int, val id: Int, val title: String, val body: String)