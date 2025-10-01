// Weaconditiondetail.kt
package com.example.disease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
//import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Weaconditiondetail(navController: NavController, newsId: String?) {
    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(newsId) {
        if (newsId != null) {
            isLoading = true
            val fetchedNews = fetchNewsDetailFromApi(newsId)
            if (fetchedNews != null) {
                newsItem = fetchedNews
            } else {
                error = "သတင်းအချက်အလက်များ ရယူ၍မရပါ"
            }
            isLoading = false
        } else {
            error = "သတင်းအချက်အလက် ID မရှိပါ"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "သတင်းအချက်အလက်",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "နောက်သို့",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff254365)
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("သတင်းအချက်အလက်များ ရယူနေသည်...")
            }
        } else if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error!!, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("နောက်သို့ပြန်သွားမည်", color = Color(0xff254365))
                    }
                }
            }
        } else if (newsItem != null) {
            WeaconditiondetailContent(
                newsItem = newsItem!!,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun WeaconditiondetailContent(newsItem: NewsItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        if (newsItem.coverImageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.coverImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "သတင်းဓာတ်ပုံ",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "ဓာတ်ပုံမရှိ",
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = newsItem.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(bottom = 20.dp),
                lineHeight = 28.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "အမျိုးအစား",
                        tint = Color(0xff254365),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = newsItem.category,
                        fontSize = 14.sp,
                        color = Color(0xff254365),
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "ရက်စွဲ",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatDate(newsItem.createdAt),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${newsItem.viewsCount} ကြိမ်",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = newsItem.description.ifEmpty { "ဤသတင်းတွင် ရှင်းလင်းချက်မရှိပါ။" },
                fontSize = 16.sp,
                color = Color(0xFF333333),
                lineHeight = 24.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

private suspend fun fetchNewsDetailFromApi(newsId: String): NewsItem? = withContext(Dispatchers.IO) {
    return@withContext try {
        val apiUrl = "https://api.mmweather.org/mobile/latest-posts?page=1"
        val jsonString = URL(apiUrl).readText()
        val jsonObject = JSONObject(jsonString)
        val dataArray = jsonObject.getJSONArray("data")

        for (i in 0 until dataArray.length()) {
            val item = dataArray.getJSONObject(i)
            val id = item.getString("id")

            if (id == newsId) {
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

                return@withContext NewsItem(
                    id = id,
                    title = title,
                    description = description,
                    coverImageUrl = coverImageUrl,
                    category = category,
                    createdAt = createdAt,
                    viewsCount = viewsCount
                )
            }
        }
        null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}