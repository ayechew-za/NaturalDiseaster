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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.disease.data.Category

@Composable
fun HorizontalScroll(repository: com.example.disease.data.repo.AnnouncementRepository,navController: NavController) {

    var categories by remember { mutableStateOf<List<Category>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(key1 = Unit) {
        val result = repository.getCategories()
        isLoading = false
        if (result.isSuccess) {
            categories = result.getOrNull()?.data
        } else {
            error = result.exceptionOrNull()?.message
        }
    }

    // Main container with proper scrolling behavior
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fixed content at the top
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Categories",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Show loading, error, or categories
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
                categories.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No categories found",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    // Use LazyRow for horizontal scrolling of categories
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        items(categories!!) { category ->
                            CategoryItem(
                                category = category,
                                isSelected = selectedCategory?.id == category.id,
                                onCategoryClick = {
                                    // Toggle selection - if same category clicked again, deselect it
                                    selectedCategory = if (selectedCategory?.id == category.id) {
                                        null
                                    } else {
                                        category
                                    }
                                }
                            )
                        }
                    }
                }
            }

            WeatherCondition(navController = navController)
            // Add spacer to ensure content doesn't get hidden behind the fixed box
            if (selectedCategory != null) {
                Spacer(modifier = Modifier.height(180.dp)) // Reserve space for the fixed box
            }
        }

        // Fixed SelectedCategoryBox at the bottom (always visible)
        if (selectedCategory != null) {
            SelectedCategoryBox(selectedCategory = selectedCategory!!)
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onCategoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(132.dp)
            .height(80.dp)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                if (isSelected) Color(0xFFE3F2FD) else Color(0xFFFFFFFF)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onCategoryClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            // Display category icon from API
            if (!category.icon.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(category.icon)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = category.name,
                    modifier = Modifier.size(30.dp, 30.dp)
                )
            } else {
                // Fallback icon if no icon from API
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/one.svg") // Your fallback asset
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "Default Icon",
                    modifier = Modifier.size(32.dp, 32.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = category.name ?: "Unknown",
                fontSize = 8.sp,
                color = Color(0xFF1A1A1A),
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
@Composable
fun SelectedCategoryBox(selectedCategory: Category) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5))
                .border(
                    width = 2.dp,
                    color = Color(0xFF2196F3),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                // Header
                Text(
                    text = "Selected Category Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Main icon
                if (!selectedCategory.icon.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedCategory.icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = selectedCategory.name,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 12.dp)
                    )
                }

                // Main details
                Text(
                    text = "Name: ${selectedCategory.name ?: "Unknown"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "ID: ${selectedCategory.id ?: "N/A"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Slug: ${selectedCategory.slug ?: "-"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Subcategories
                val children = selectedCategory.children ?: emptyList()
                if (children.isNotEmpty()) {
                    Text(
                        text = "Subcategories:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(children) { child ->
                            Column(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .width(80.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (!child.icon.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(child.icon)
                                            .decoderFactory(SvgDecoder.Factory())
                                            .build(),
                                        contentDescription = child.name ?: "Subcategory",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(bottom = 4.dp)
                                    )
                                }
                                Text(
                                    text = child.name ?: "-",
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No subcategories available.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
