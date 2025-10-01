package com.example.disease.screens

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.disease.data.Category
import com.example.disease.data.repo.CategoryRepository

@Composable
fun CategoryListScreen() {
    val categories = remember { mutableStateOf<List<Category>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val selectedCategory = remember { mutableStateOf<Category?>(null) }
    val expandedCategories = remember { mutableStateOf(mutableSetOf<String>()) }

    LaunchedEffect(Unit) {
        isLoading.value = true
        val repository = CategoryRepository()
        val fetchedCategories = repository.fetchCategories()
        categories.value = fetchedCategories
        isLoading.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF))
    ) {
        Text(
            text = "ကဏ္ဍများ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )

        selectedCategory.value?.let { category ->
            SelectedCategoryBox(selectedCategory = category)
        }

        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("ကဏ္ဍများ Loading...", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(categories.value) { category ->
                    CategoryItem(
                        category = category,
                        isExpanded = expandedCategories.value.contains(category.id),
                        onExpandedChange = { categoryId ->
                            expandedCategories.value = expandedCategories.value.toMutableSet().apply {
                                if (contains(categoryId)) remove(categoryId) else add(categoryId)
                            }
                        },
                        onCategorySelected = { selectedCategory.value = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isExpanded: Boolean,
    onExpandedChange: (String) -> Unit,
    onCategorySelected: (Category) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .clickable {
                    if (!category.children.isNullOrEmpty()) {
                        onExpandedChange(category.id ?: "")
                    } else {
                        onCategorySelected(category)
                    }
                }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(category.icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${category.name} icon",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = category.name ?: "Unknown Category",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )

                if (!category.children.isNullOrEmpty()) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowRight,
                        contentDescription = if (isExpanded) "ပိတ်ရန်" else "ဖွင့်ရန်",
                        tint = Color(0xFF2196F3)
                    )
                }
            }
        }

        if (isExpanded && !category.children.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(start = 32.dp)
            ) {
                category.children.forEach { childCategory ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE3F2FD))
                            .clickable { onCategorySelected(childCategory) }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(childCategory.icon)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${childCategory.name} icon",
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = childCategory.name ?: "Unknown",
                                fontSize = 14.sp,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun ButtonCategoryBox(selectedCategory: Category) {
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
                Text(
                    text = "Selected Category Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Text 1: ${selectedCategory.name ?: "Unknown"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Text 2: Category ID - ${selectedCategory.id ?: "N/A"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Text 3: Additional information about ${selectedCategory.name ?: "this category"}",
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}
