package com.example.disease.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit = {},
    onCloseDrawer: () -> Unit = {}
) {
    // Track which item is expanded
    var expandedItem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "ပင်မစာမျက်နှာ",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp),
            color = Color(0xFF254365),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Drawer Items
        val items = listOf(
            "မိုးလေဝသခန့်မှန်းချက်များ",
            "၄ရက်စာမိုးလေဝသအခြေအနေ ",
            "မိုးလေဝသဘေးအန္တရာယ်",
            "ဘူမိဗေဒဆိုင်ရာဘေးအန္တရာယ်", // <- Has sub-items
            "ဇလဗေဒဆိုင်ရာဘေးအန္တရာယ်",
            "ရာသီဥတုဆိုင်ရာဘေးအန္တရာယ်",
            "သဘာဝဘေးသိမှတ်စရာများ",
            " အခြေခံမိုးလေဝသပညာလေ့လာခြင်း",
            "မြေပုံများမှလေ့လာခြင်း",
            "အသိပညာပေး"
        )

        items.forEach { title ->
            val isExpandable = title == "ဘူမိဗေဒဆိုင်ရာဘေးအန္တရာယ်"
            val isExpanded = expandedItem == title

            DrawerItem(
                title = title,
                isExpanded = isExpanded,
                isExpandable = isExpandable,
                onItemClick = {
                    if (isExpandable) {
                        expandedItem = if (isExpanded) null else title
                    } else {
                        onItemSelected(title)
                    }
                }
            )

            // Only show sub-items if this is the expanded parent
            if (title == "ဘူမိဗေဒဆိုင်ရာဘေးအန္တရာယ်" && isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    // Sub-item boxes with Burmese text content
                    SubItemBox(
                        text = "ငလျင်ဘေးအန္တရာယ်",
                        onClick = { onItemSelected("ငလျင်ဘေးအန္တရာယ်") }
                    )
                    SubItemBox(
                        text = "မြေပြိုဘေးအန္တရာယ်",
                        onClick = { onItemSelected("မြေပြိုဘေးအန္တရာယ်") }
                    )
                    SubItemBox(
                        text = "မီးတောင်ပေါက်ကွဲမှုဘေး",
                        onClick = { onItemSelected("မီးတောင်ပေါက်ကွဲမှုဘေး") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerItem(
    title: String,
    isExpanded: Boolean,
    isExpandable: Boolean = false,
    onItemClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // Fixed height for consistent size
            .padding(vertical = 4.dp)
            .background(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        TextButton(
            onClick = onItemClick,
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
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    maxLines = 2, // Allow 2 lines for long text
                    overflow = TextOverflow.Ellipsis // Show ellipsis if text is too long
                )
                // ALWAYS show arrow for every item
                Icon(
                    imageVector = if (isExpandable) {
                        // For expandable items, show dropdown or right arrow
                        if (isExpanded) Icons.Filled.ArrowDropDown else Icons.Filled.KeyboardArrowRight
                    } else {
                        // For non-expandable items, always show right arrow
                        Icons.Filled.KeyboardArrowRight
                    },
                    contentDescription = if (isExpandable) {
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
}

@Composable
fun SubItemBox(
    text: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp) // Fixed height for consistent size (slightly smaller than main items)
            .padding(vertical = 4.dp)
            .background(
                color = Color(0xFFD6EAF8), // Slightly different blue for sub-items
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF1A5276), // Darker blue for sub-item text
                containerColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    maxLines = 2, // Allow 2 lines for long text
                    overflow = TextOverflow.Ellipsis // Show ellipsis if text is too long
                )
                // Add arrow for sub-items as well
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