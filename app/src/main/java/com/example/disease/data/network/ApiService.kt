package com.example.disease.data.network


import com.example.disease.data.AnnouncementResponse
import com.example.disease.data.CategoryResponse
import com.example.disease.data.PostResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("mobile/announcements")
    suspend fun getAnnouncements(): AnnouncementResponse

    @GET("mobile/categories")
    suspend fun getCategories(): CategoryResponse


    // Posts အတွက် API - type နဲ့ categoryId အလိုက်
    @GET("mobile/posts/search")
    suspend fun getPostsByTypeAndCategory(
        @Query("categoryId") categoryId: String? = null,
        @Query("query") type: String? = null
    ): PostResponse


    // Latest posts အတွက် API
    @GET("mobile/latest-posts")
    suspend fun getLatestPosts(
        @Query("categoryId") categoryId: String? = null,
        @Query("type") type: String? = null
    ): PostResponse



    // အသစ်ထပ်ထည့်ရမယ့် function
    @GET("mobile/posts/search")
    suspend fun getPostsByCategoryAndType(
        @Query("categoryId") categoryId: String?,
        @Query("query") query: String?
    ): AnnouncementResponse

   // companion object
}