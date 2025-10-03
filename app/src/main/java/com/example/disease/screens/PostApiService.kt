// PostApiService.kt
package com.example.disease.screens

import com.example.disease.data.Post
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object PostApiService {
    private val gson = Gson()

    suspend fun getPostDetail(postId: String): Post? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.mmweather.org/mobile/posts/$postId")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15_000
            connection.readTimeout = 15_000
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            println("🔍 API Response Code: $responseCode for postId: $postId")

            if (responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                println("🔍 API Response: $responseText")

                // Parse with Gson
                val jsonObject = gson.fromJson(responseText, com.google.gson.JsonObject::class.java)

                // Extract data object
                val dataObject = if (jsonObject.has("data")) {
                    jsonObject.getAsJsonObject("data")
                } else {
                    jsonObject
                }

                // Convert to Post using Gson
                val post = gson.fromJson(dataObject, Post::class.java)

                // Debug logging
                println("🔍 Parsed Post - Title: ${post.title}")
                println("🔍 Parsed Post - Created At: ${post.createdAt}")
                println("🔍 Parsed Post - Cover Image: ${post.coverImage?.url}")
                println("🔍 Parsed Post - Description Images: ${post.descriptionImages?.size}")
                post.descriptionImages?.forEachIndexed { index, image ->
                    println("🔍   Image $index: ${image.url}")
                }

                return@withContext post
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() }
                println("🔍 API Error Response: $errorText")
                null
            }
        } catch (e: Exception) {
            println("🔍 API Exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}