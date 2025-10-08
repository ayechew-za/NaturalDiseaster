package com.example.disease.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.disease.R
import com.example.disease.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // 3 စက္ကန့်ကြာပြီးနောက် Home Screen ကိုသွားမယ်
    LaunchedEffect(Unit) {
        delay(3000) // 3000 milliseconds = 3 seconds
        navController.navigate(Screen.Home.route) {
            // Splash screen ကို back stack ကနေဖယ်ရှားမယ်
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    // Splash Screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)), // Purple color
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // App Icon or Image
            Image(
                painter = painterResource(id = R.drawable.splash1),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            // App Name
            Text(
                text = "Disease App",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Welcome Text in Myanmar
            Text(
                text = "ကျန်းမာရေး ရောဂါများ ရှာဖွေရန်",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Loading Text
            Text(
                text = "လုပ်ဆောင်နေသည်...",
                color = Color(0xFFBB86FC), // Light purple
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}