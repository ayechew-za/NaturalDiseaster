package com.example.disease.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weatherapp.ui.screen.TopAppBarWithBackButton

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"  // ပထမဆုံး Home Screen ကနေစမယ်
    ) {
        // Home Screen
        composable("home") {
            Home(
                onAnnounceClick = {
                    navController.navigate("announceui")  // Announcement Screen ကိုသွားမယ်
                },
                navController = navController
            )
        }

        // Announcement Screen
        composable("announceui") {
            TopAppBarWithBackButton(
                title = "မြန်မာနိုင်ငံလတ်တလောရာသီဥတုအခြေအနေ",
                onBackClick = { navController.popBackStack() }  // နောက်ပြန်သွားမယ်
            )
        }

        // Weather Condition Detail Screen
        composable(
            "weaconditiondetail/{newsId}",
            arguments = listOf(navArgument("newsId") { type = NavType.StringType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId")
            Weaconditiondetail(
                navController = navController,
                newsId = newsId
            )
        }
    }
}