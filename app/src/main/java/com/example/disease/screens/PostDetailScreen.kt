// PostDetailScreen.kt
package com.example.disease.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.disease.data.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(postId: String, navController: NavController) {
    var post by remember { mutableStateOf<Post?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(postId) {
        Log.d("PostDetail", "Fetching post with ID: $postId")
        try {
            val result = PostApiService.getPostDetail(postId)
            post = result

            // Debug logging
            Log.d("PostDetail", "Post fetched successfully: ${result != null}")
            Log.d("PostDetail", "Post title: ${result?.title}")
            Log.d("PostDetail", "Post createdAt: ${result?.createdAt}")
            Log.d("PostDetail", "Post description length: ${result?.description?.length}")
            Log.d("PostDetail", "Post coverImage: ${result?.coverImage?.url}")
            Log.d("PostDetail", "Post descriptionImages count: ${result?.descriptionImages?.size}")

            if (result == null) {
                errorMessage = "Post not found"
            }
        } catch (e: Exception) {
            Log.e("PostDetail", "Error fetching post: ${e.message}")
            errorMessage = "Network error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("သတင်းအပြည့်အစုံ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("လုပ်ဆောင်နေသည်...")
                        Text("Post ID: $postId", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("အမှားတစ်ခုဖြစ်နေသည်", color = Color.Red, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMessage ?: "Unknown error")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Post ID: $postId", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            post != null -> {
                PostDetailContent(post = post!!, innerPadding = innerPadding)
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("သတင်းရှာမတွေ့ပါ", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Post ID: $postId", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun PostDetailContent(post: Post, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Try coverImage first, then first description image as fallback
        val imageUrl = post.coverImage?.url ?: post.descriptionImages?.firstOrNull()?.url

        imageUrl?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(model = url),
                contentDescription = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Title
        Text(
            text = post.title ?: "No Title",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date
        post.createdAt?.let { createdAt ->
            if (createdAt.isNotEmpty() && createdAt.length >= 10) {
                Text(
                    text = "ရက်စွဲ: ${createdAt.substring(0, 10)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "ရက်စွဲ: $createdAt",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Category
        post.category?.name?.let { categoryName ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Category: $categoryName",
                fontSize = 14.sp,
                color = Color.Blue,
                fontWeight = FontWeight.Medium
            )
        }

        // Views count
        post.viewsCount?.let { views ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Views: $views",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description with HTML tags removed
        val cleanDescription = removeHtmlTags(post.description ?: "No Description Available")
        Text(
            text = cleanDescription,
            fontSize = 16.sp,
            color = Color(0xFF444444),
            lineHeight = 24.sp
        )

        // Show all description images
        post.descriptionImages?.takeIf { it.isNotEmpty() }?.let { images ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Images:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))

            images.forEach { image ->
                image.url?.let { url ->
                    Image(
                        painter = rememberAsyncImagePainter(model = url),
                        contentDescription = "Post image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Tags
        post.tags?.takeIf { it.isNotEmpty() }?.let { tags ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tags: ${tags.joinToString { it.name ?: "" }}",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

// Function to remove HTML tags from description
fun removeHtmlTags(htmlString: String?): String {
    if (htmlString.isNullOrEmpty()) return "No Description Available"

    return htmlString
        .replace(Regex("<.*?>"), "") // Remove all HTML tags
        .replace(Regex("&nbsp;"), " ") // Replace &nbsp; with space
        .replace(Regex("&amp;"), "&") // Replace &amp; with &
        .replace(Regex("&lt;"), "<") // Replace &lt; with <
        .replace(Regex("&gt;"), ">") // Replace &gt; with >
        .replace(Regex("&quot;"), "\"") // Replace &quot; with "
        .replace(Regex("&#39;"), "'") // Replace &#39; with '
        .trim() // Remove leading/trailing whitespace
}