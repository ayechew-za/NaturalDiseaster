package com.example.disease.data.repo

// CategoryRepository.kt
import com.example.disease.data.Category
//import com.example.disease.data.WeatherCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class CategoryRepository {
    suspend fun fetchCategories(): List<Category> = withContext(Dispatchers.IO) {
        return@withContext try {
            val apiUrl = "https://api.mmweather.org/mobile/categories"
            val jsonString = URL(apiUrl).readText()
            val jsonObject = JSONObject(jsonString)
            val dataArray = jsonObject.getJSONArray("data")

            parseCategories(dataArray)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseCategories(dataArray: org.json.JSONArray): List<Category> {
        val categories = mutableListOf<Category>()

        for (i in 0 until dataArray.length()) {
            val item = dataArray.getJSONObject(i)
            categories.add(parseCategory(item))
        }

        return categories
    }
    private fun parseCategory(item: org.json.JSONObject): Category {
        return Category(
            id = item.optString("id").takeIf { it != "null" },
            name = item.optString("name").takeIf { it != "null" },
            slug = item.optString("slug").takeIf { it != "null" },
            icon = item.optString("icon").takeIf { it != "null" },
            parentId = item.optString("parent_id").takeIf { it != "null" },
            level = item.optInt("level"),
            children = if (item.has("children") && !item.isNull("children")) {
                parseCategories(item.getJSONArray("children"))
            } else {
                emptyList()
            }
        )
    }

}