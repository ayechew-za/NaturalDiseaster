package com.example.disease.data.repo

import com.example.disease.data.AnnouncementResponse
import com.example.disease.data.CategoryResponse
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
}