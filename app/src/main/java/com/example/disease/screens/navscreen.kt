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

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    repository: com.example.disease.data.repo.AnnouncementRepository,
    onItemSelected: (String, String?) -> Unit = { _, _ -> }, // Accept category name and type
    onCloseDrawer: () -> Unit = {}
) {
    var categories by remember { mutableStateOf<List<com.example.disease.data.Category>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var expandedItem by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<com.example.disease.data.Category?>(null) }
    var showTypeSelection by remember { mutableStateOf(false) }

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
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "ပင်မစာမျက်နှာ",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp),
            color = Color(0xFF254365),
            modifier = Modifier.padding(bottom = 24.dp)
        )

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
                categories!!.forEach { category ->
                    val hasChildren = !category.children.isNullOrEmpty()
                    val isExpanded = expandedItem == category.id

                    // Main Category Item
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp)
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
                                    showTypeSelection = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF254365),
                                containerColor = Color.Transparent
                            )
                        )


                        {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize()
                            )


                            {



                                Text(
                                    text = category.name ?: "Unknown",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
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
                                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            category.children!!.forEach { child ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .padding(vertical = 4.dp)
                                        .background(
                                            color = Color(0xFFD6EAF8),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                ) {
                                    TextButton(
                                        onClick = {
                                            selectedCategory = child
                                            showTypeSelection = true
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
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
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

        Spacer(modifier = Modifier.weight(1f))


    }

    // Type Selection Dialog
    if (showTypeSelection && selectedCategory != null) {
        AlertDialog(
            onDismissRequest = {
                showTypeSelection = false
                selectedCategory = null
            },
            title = {
                Text(
                    text = "Select Content Type",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select content type for: ${selectedCategory?.name ?: "Unknown"}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Post option
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE3F2FD))
                            .border(
                                width = 1.dp,
                                color = Color(0xFF2196F3),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onItemSelected(selectedCategory?.name ?: "Unknown", "post")
                                showTypeSelection = false
                                selectedCategory = null
                                onCloseDrawer()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "သတင်းအသစ်များ",
                            fontSize = 14.sp,
                            color = Color(0xFF2196F3),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Knowledge option
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8F5E8))
                            .border(
                                width = 1.dp,
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onItemSelected(selectedCategory?.name ?: "Unknown", "knowledge")
                                showTypeSelection = false
                                selectedCategory = null
                                onCloseDrawer()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "ဗဟုသုတများ",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTypeSelection = false
                        selectedCategory = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}