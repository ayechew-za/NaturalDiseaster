package com.example.disease.data.repo

import com.example.disease.data.AnnouncementResponse
import com.example.disease.data.CategoryResponse
import com.example.disease.data.PostResponse
import com.example.disease.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AnnouncementRepository(private val apiService: ApiService) {

    suspend fun getAnnouncements(): Result<AnnouncementResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAnnouncements()
                Result.success(response)
            } catch (e: IOException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message()}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }

    suspend fun getCategories(): Result<CategoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCategories()
                Result.success(response)
            } catch (e: IOException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message()}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }

    suspend fun getPostsByCategoryAndType(categoryId: String, type: String): Result<PostResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPostsByTypeAndCategory(categoryId, type)
                Result.success(response)
            } catch (e: IOException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message()}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }

    suspend fun getLatestPosts(categoryId: String? = null, type: String? = null): Result<PostResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLatestPosts(categoryId, type)
                Result.success(response)
            } catch (e: IOException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message()}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }

    // ဒီ function ကို class ထဲမှာ သေချာသတ်မှတ်ပါမယ်
    suspend fun getPostsByCategory(categoryId: String, type: String? = null): Result<PostResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (type != null) {
                    // type ရှိရင် getPostsByTypeAndCategory ကိုသုံးမယ်
                    apiService.getPostsByTypeAndCategory(categoryId, type)
                } else {
                    // type မရှိရင် getLatestPosts ကိုသုံးမယ်
                    apiService.getLatestPosts(categoryId, null)
                }
                Result.success(response)
            } catch (e: IOException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message()}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }

    // Knowledge posts တွေအတွက် အထူး function
    suspend fun getKnowledgePosts(categoryId: String? = null): Result<PostResponse> {
        return getLatestPosts(categoryId, "knowledge")
    }

    // News posts တွေအတွက် အထူး function
    suspend fun getNewsPosts(categoryId: String? = null): Result<PostResponse> {
        return getLatestPosts(categoryId, "post")
    }
}