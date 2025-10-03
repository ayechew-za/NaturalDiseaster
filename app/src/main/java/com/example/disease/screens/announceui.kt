package com.example.weatherapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.disease.data.AnnouncementResponse
import com.example.weatherapp.ui.viewmodel.AnnouncementUiState
import com.example.weatherapp.ui.viewmodel.AnnouncementViewModel

@Composable
fun AnnounceUI(navController: NavController) {
    val viewModel: AnnouncementViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (uiState) {
            is AnnouncementUiState.Loading -> LoadingState()
            is AnnouncementUiState.Success -> {
                AnnouncementContent(
                    response = (uiState as AnnouncementUiState.Success).response,
                    onRetry = viewModel::retry
                )
            }

            is AnnouncementUiState.Error -> {
                ErrorState(
                    message = (uiState as AnnouncementUiState.Error).message,
                    onRetry = viewModel::retry
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun AnnouncementContent(response: AnnouncementResponse, onRetry: () -> Unit) {
    val announcement = response.data
    if (announcement == null) {
        Text(
            text = "No announcement data available",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        return
    }
    val style = announcement.style
    if (style == null) {
        Text(
            text = "Announcement style info missing",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(android.graphics.Color.parseColor(style.bgColor))
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                announcement.text?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

private fun formatDate(dateString: String?): String? {
    return try {
        dateString?.substring(0, 10)
    } catch (e: Exception) {
        dateString
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackButton(
    title: String,
    navController: NavController,
    onBackClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xffEEF5FE)
            )
        )

        // Place AnnounceUI BELOW the TopAppBar
        AnnounceUI(navController = navController)
    }
}
