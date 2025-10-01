package com.example.disease.data.network


import com.example.disease.data.AnnouncementResponse
import com.example.disease.data.CategoryResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("mobile/announcements")
    suspend fun getAnnouncements(): AnnouncementResponse

    @GET("mobile/categories")
    suspend fun getCategories(): CategoryResponse



}