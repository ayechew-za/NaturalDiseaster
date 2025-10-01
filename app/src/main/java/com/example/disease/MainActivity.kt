package com.example.disease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.disease.screens.AppNavigation
import com.example.disease.ui.theme.DiseaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiseaseTheme {
                // Navigation controller ကို သုံးမယ်
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiseasePreview() {
    DiseaseTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}