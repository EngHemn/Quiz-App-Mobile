package com.example.quizapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object QuizPlay : Screen("quiz_play_screen/{categoryId}") {
        fun createRoute(categoryId: String) = "quiz_play_screen/$categoryId"
    }
}
