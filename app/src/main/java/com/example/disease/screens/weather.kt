package com.example.disease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.disease.navigation.Screen

@Composable
fun HorizontalScroll(
    repository: com.example.disease.data.repo.AnnouncementRepository,
    navController: NavController
) {

    var categories by remember { mutableStateOf<List<Category>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expandedSubcategories by remember { mutableStateOf<MutableSet<String>>(mutableSetOf()) }

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
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        items(categories!!) { category ->
                            CategoryItem(
                                category = category,
                                isSelected = selectedCategory?.id == category.id,
                                onCategoryClick = {
                                    selectedCategory = if (selectedCategory?.id == category.id) {
                                        null
                                    } else {
                                        category
                                    }
                                    // Reset expanded subcategories when category changes
                                    expandedSubcategories.clear()
                                }
                            )
                        }
                    }
                }
            }

            WeatherCondition(
                navController = navController,
                onNewClick = {
                    navController.navigate("latestNew")
                }
            )


            if (selectedCategory != null) {
                Spacer(modifier = Modifier.height(180.dp))
            }
        }

        // Fixed SelectedCategoryBox at the bottom
        if (selectedCategory != null) {
            SelectedCategoryBox(
                selectedCategory = selectedCategory!!,
                expandedSubcategories = expandedSubcategories,
                onExpandedChange = { subcategoryId ->
                    val newSet = expandedSubcategories.toMutableSet()
                    if (newSet.contains(subcategoryId)) {
                        newSet.remove(subcategoryId)
                    } else {
                        newSet.add(subcategoryId)
                    }
                    expandedSubcategories = newSet
                },
                onTypeSelected = { category, type ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(
                            categoryId = category.id ?: "",
                            type = type
                        )
                    )
                    selectedCategory = null
                    expandedSubcategories.clear()
                },
                onChildTypeSelected = { child, type ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(
                            categoryId = child.id ?: "",
                            type = type
                        )
                    )
                    selectedCategory = null
                    expandedSubcategories.clear()
                },
                onCancel = {
                    selectedCategory = null
                    expandedSubcategories.clear()
                }
            )
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!category.icon.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(category.icon)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = category.name,
                    modifier = Modifier.size(30.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/one.svg")
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "Default Icon",
                    modifier = Modifier.size(32.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTypeDropdown(
    onTypeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Simple dropdown trigger - just the dropdown icon
        Icon(
            imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowRight,
            contentDescription = "Select type",
            modifier = Modifier
                .size(24.dp)
                .clickable { expanded = true },
            tint = Color.Gray
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "သတင်းအသစ်များ",
                        fontSize = 12.sp
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected("post")
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "ဗဟုသုတများ",
                        fontSize = 12.sp
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected("knowledge")
                }
            )
        }
    }
}

@Composable
fun TypeOptionsRow(
    onTypeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Select Content Type:",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Post option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE3F2FD))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF2196F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTypeSelected("post") }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "သတင်းအသစ်များ",
                    fontSize = 12.sp,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Knowledge option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE8F5E8))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTypeSelected("knowledge") }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "ဗဟုသုတများ",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SelectedCategoryBox(
    selectedCategory: Category,
    expandedSubcategories: Set<String>,
    onExpandedChange: (String) -> Unit,
    onTypeSelected: (Category, String) -> Unit,
    onChildTypeSelected: (Category, String) -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8FD))
                    .padding(16.dp)
            ) {
                // Title row with cancel button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory.name ?: "Unknown",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    // Cancel button
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onCancel() },
                        tint = Color.Gray
                    )
                }

                // Main category row with icon, text, and dropdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFCBE1FA),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!selectedCategory.icon.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(selectedCategory.icon)
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            contentDescription = selectedCategory.name,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Text(
                        text = "All ${selectedCategory.name ?: "Types"}",
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f)
                    )

                    SimpleTypeDropdown(
                        onTypeSelected = { type ->
                            onTypeSelected(selectedCategory, type)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Children categories (subcategories)
                val children = selectedCategory.children ?: emptyList()
                if (children.isNotEmpty()) {
                    Text(
                        text = "Subcategories:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        items(children) { child ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                // Subcategory header row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFCBE1FA),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                        .clickable {
                                            onExpandedChange(child.id ?: "")
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!child.icon.isNullOrEmpty()) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(child.icon)
                                                .decoderFactory(SvgDecoder.Factory())
                                                .build(),
                                            contentDescription = child.name,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }

                                    Text(
                                        text = child.name ?: "-",
                                        fontSize = 14.sp,
                                        color = Color(0xFF333333),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Icon(
                                        imageVector = if (expandedSubcategories.contains(child.id))
                                            Icons.Default.ArrowDropDown
                                        else
                                            Icons.Default.KeyboardArrowRight,
                                        contentDescription = if (expandedSubcategories.contains(child.id))
                                            "Collapse"
                                        else
                                            "Expand",
                                        modifier = Modifier
                                            .size(24.dp),
                                        tint = Color.Gray
                                    )
                                }

                                // Type options that appear when subcategory is expanded
                                if (expandedSubcategories.contains(child.id)) {
                                    TypeOptionsRow(
                                        onTypeSelected = { type ->
                                            onChildTypeSelected(child, type)
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No subcategories available.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

