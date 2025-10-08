package com.example.disease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LatestNew(navController: NavController) {
    var newsList by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        val fetchedNews = fetchNewsFromApi()
        newsList = fetchedNews
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "နောက်ဆုံးရသတင်းများ",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "သတင်းများ လုပ်ဆောင်နေသည်...",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        } else if (newsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "သတင်းများ မရှိပါ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(newsList) { newsItem ->
                    LatestNewsItem(
                        newsItem = newsItem,
                        onItemClick = {
                            val safeNewsId = newsItem.id.replace("/", "_")
                            navController.navigate("weaconditiondetail/$safeNewsId")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun LatestNewsItem(newsItem: NewsItem, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onItemClick)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (newsItem.coverImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(newsItem.coverImageUrl)
                        .build(),
                    contentDescription = "သတင်းဓာတ်ပုံ",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "ဓာတ်ပုံမရှိ",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = newsItem.title,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = newsItem.category,
                        fontSize = 12.sp,
                        color = Color(0xff254365),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = TodayDate(newsItem.createdAt),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

private suspend fun fetchNewsFromApi(): List<NewsItem> = withContext(Dispatchers.IO) {
    try {
        val apiUrl = "https://api.mmweather.org/mobile/latest-posts?page=1"
        val jsonString = URL(apiUrl).readText()
        val jsonObject = JSONObject(jsonString)
        val dataArray = jsonObject.getJSONArray("data")

        val newsList = mutableListOf<NewsItem>()

        for (i in 0 until dataArray.length()) {
            val item = dataArray.getJSONObject(i)
            val id = item.getString("id")
            val title = item.getString("title")
            val description = item.getString("description")

            val coverImageUrl = if (item.has("cover_image") && !item.isNull("cover_image")) {
                val coverImage = item.getJSONObject("cover_image")
                coverImage.getString("url")
            } else {
                ""
            }

            val category = if (item.has("category") && !item.isNull("category")) {
                val categoryObj = item.getJSONObject("category")
                categoryObj.getString("name")
            } else {
                "မိုးလေဝသ"
            }

            val createdAt = item.getString("created_at")
            val viewsCount = item.getInt("views_count")

            newsList.add(
                NewsItem(
                    id = id,
                    title = title,
                    description = description,
                    coverImageUrl = coverImageUrl,
                    category = category,
                    createdAt = createdAt,
                    viewsCount = viewsCount
                )
            )
        }
        newsList
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

fun TodayDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        dateString
    }
}