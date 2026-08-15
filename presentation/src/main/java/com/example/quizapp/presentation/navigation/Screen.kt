package com.example.quizapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Main : Screen("main_screen")
    object Home : Screen("home_screen")
    object Category : Screen("category_screen")
    object AddQuiz : Screen("add_quiz_screen")
    object Favorite : Screen("favorite_screen")
    object Profile : Screen("profile_screen")
    object QuizPlay : Screen("quiz_play_screen/{categoryId}") {
        fun createRoute(categoryId: String) = "quiz_play_screen/$categoryId"
    }
}
