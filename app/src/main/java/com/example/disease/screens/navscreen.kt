package com.example.disease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import java.nio.file.WatchEvent

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    repository: com.example.disease.data.repo.AnnouncementRepository,
    onItemSelected: (String, String?) -> Unit = { _, _ -> },
    onCloseDrawer: () -> Unit = {}
) {
    var categories by remember { mutableStateOf<List<com.example.disease.data.Category>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var expandedItem by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<com.example.disease.data.Category?>(null) }
    var expandedTypeDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        val result = repository.getCategories()
        isLoading = false
        if (result.isSuccess) {
            categories = result.getOrNull()?.data
        } else {
            error = result.exceptionOrNull()?.message
        }
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color(0xffF7FBFE))



    .verticalScroll(rememberScrollState())
            .padding(top = 50.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/logoo.png")
                .build(),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 24.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Natural Disaster",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))


        Divider(modifier = Modifier.fillMaxWidth())
        // Add spacing after logo
        Spacer(modifier = Modifier.height(24.dp))



        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF254365)
                    )
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
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
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No categories found",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categories!!.forEach { category ->
                        val hasChildren = !category.children.isNullOrEmpty()
                        val isExpanded = expandedItem == category.id

                        // Main Category Item
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(6.dp)
                                )
                        ) {
                            TextButton(
                                onClick = {
                                    if (hasChildren) {
                                        expandedItem = if (isExpanded) null else category.id
                                    } else {
                                        selectedCategory = category
                                        expandedTypeDropdown = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFF254365),
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = category.name ?: "Unknown",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Icon(
                                        imageVector = if (hasChildren) {
                                            if (isExpanded) Icons.Filled.ArrowDropDown else Icons.Filled.KeyboardArrowRight
                                        } else {
                                            Icons.Filled.KeyboardArrowRight
                                        },
                                        contentDescription = if (hasChildren) {
                                            if (isExpanded) "Expanded" else "Collapsed"
                                        } else {
                                            "Navigate"
                                        },
                                        tint = Color(0xFF254365),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Subcategories
                        if (hasChildren && isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                category.children!!.forEach { child ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .background(
                                                color = Color(0xFFD6EAF8),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                    ) {
                                        TextButton(
                                            onClick = {
                                                selectedCategory = child
                                                expandedTypeDropdown = true
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 12.dp),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = Color(0xFF1A5276),
                                                containerColor = Color.Transparent
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                if (!child.icon.isNullOrEmpty()) {
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(child.icon)
                                                            .decoderFactory(SvgDecoder.Factory())
                                                            .build(),
                                                        contentDescription = child.name,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                } else {
                                                    Spacer(modifier = Modifier.size(20.dp))
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                }

                                                Text(
                                                    text = child.name ?: "Unknown",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Normal
                                                    ),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(end = 8.dp),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )

                                                Icon(
                                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                                    contentDescription = "Navigate",
                                                    tint = Color(0xFF1A5276),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add some bottom padding
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Type Selection Dropdown
    if (expandedTypeDropdown && selectedCategory != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable {
                    expandedTypeDropdown = false
                    selectedCategory = null
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "Select Content Type",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF254365),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Category: ${selectedCategory?.name ?: "Unknown"}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Dropdown style options
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                        .background(Color.White)
                ) {
                    // Post option
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                                onItemSelected(selectedCategory?.name ?: "Unknown", "post")
                                expandedTypeDropdown = false
                                selectedCategory = null
                                onCloseDrawer()
                            }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "သတင်းအသစ်များ",
                            fontSize = 14.sp,
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Divider
                    Divider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Knowledge option
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                                onItemSelected(selectedCategory?.name ?: "Unknown", "knowledge")
                                expandedTypeDropdown = false
                                selectedCategory = null
                                onCloseDrawer()
                            }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "ဗဟုသုတများ",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Cancel button
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = {
                        expandedTypeDropdown = false
                        selectedCategory = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Cancel", fontSize = 14.sp)
                }
            }
        }
    }
}