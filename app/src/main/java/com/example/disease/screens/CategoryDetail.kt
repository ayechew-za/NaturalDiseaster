package com.example.disease.screens




import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.disease.data.Post
import com.example.disease.data.repo.AnnouncementRepository
import com.example.disease.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: String,
    type: String,
    repository: AnnouncementRepository
) {
    var posts by remember { mutableStateOf<List<Post>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Screen Title based on type
    val screenTitle = when (type) {
        "new" -> "သတင်းအသစ်များ"
        "knowledge" -> "ဗဟုသုတများ"
        else -> "အကြောင်းအရာများ"
    }

    LaunchedEffect(key1 = categoryId) {
        coroutineScope.launch {
            val result = repository.getLatestPosts(categoryId, type)
            isLoading = false
            if (result.isSuccess) {
                posts = result.getOrNull()?.data
            } else {
                error = result.exceptionOrNull()?.message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = screenTitle,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }
                posts.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ဒီ Category အတွက် မည်သည့် Post မှ မရှိပါ",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(posts!!) { post ->
                            PostItem(post = post, navController = navController)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}





// PostItem.kt
@Composable
fun PostItem(post: Post, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                post.id?.let { id ->
                    navController.navigate(Screen.PostDetail.createRoute(id))
                }
            }
            .background(Color(0xFFFFFAFA))
            .padding(12.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFEEF5FE),
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cover Image
        post.coverImage?.url?.let { imageUrl ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = post.title,
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Texts
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = post.title ?: "No Title",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(6.dp))

            post.createdAt?.let { createdAt ->
                if (createdAt.isNotEmpty() && createdAt.length >= 10) {
                    Text(
                        text = "Created: ${createdAt.substring(0, 10)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "Created: $createdAt",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}














@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackButton(
    title: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}