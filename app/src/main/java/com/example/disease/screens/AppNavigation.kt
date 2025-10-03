package com.example.disease.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.disease.navigation.Screen
import com.example.disease.data.repo.AnnouncementRepository
import com.example.disease.data.network.RetrofitClient
import com.example.weatherapp.ui.screen.AnnounceUI

@Composable
fun AppNavigation(navController: NavHostController) {
    val repository = AnnouncementRepository(RetrofitClient.apiService)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    )
    {
        // Home Screen
        composable(Screen.Home.route) {
            Home(
                onAnnounceClick = {
                    navController.navigate(Screen.AnnounceUI.route)
                },
                navController = navController
            )
        }

        // Announcement Screen

        composable(Screen.AnnounceUI.route) {
            AnnounceUI(navController = navController)
        }

        // other routes...



        // Weather Condition Detail Screen
        composable(
            Screen.WeatherConditionDetail.route,
            arguments = listOf(navArgument("newsId") { type = NavType.StringType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId")
            Weaconditiondetail(
                navController = navController,
                newsId = newsId
            )
        }

        // Category Detail Screen (အသစ်ထည့်မယ်)
        composable(
            Screen.CategoryDetail.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""

            CategoryDetailScreen(
                navController = navController,
                categoryId = categoryId,
                type = type,
                repository = repository
            )
        }


        composable(
            Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            PostDetailScreen(postId = postId, navController = navController)
        }
    }
}




// Category Detail Screen (အသစ်ထည့်မယ်)
