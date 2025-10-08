package com.example.disease.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AnnounceUI : Screen("announceui")
    object WeatherConditionDetail : Screen("weaconditiondetail/{newsId}") {
        fun createRoute(newsId: String) = "weaconditiondetail/$newsId"
    }

    // အသစ်ထည့်မယ့် Category Detail Screen
    object CategoryDetail : Screen("category_detail/{categoryId}/{type}") {
        fun createRoute(categoryId: String, type: String) = "category_detail/$categoryId/$type"
    }

    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: String) = "post_detail/$postId"
    }


object LatestNew : Screen("latestNew")
object  Weathercondition : Screen("WeatherCondition")

}
