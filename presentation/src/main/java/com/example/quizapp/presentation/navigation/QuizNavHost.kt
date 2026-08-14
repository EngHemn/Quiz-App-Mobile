package com.example.quizapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizapp.presentation.screens.home.HomeScreen
import com.example.quizapp.presentation.screens.splash.SplashScreen
import com.example.quizapp.presentation.screens.login.LoginScreen
import com.example.quizapp.presentation.viewmodels.HomeViewModel
import com.example.quizapp.presentation.viewmodels.LoginViewModel
import com.example.quizapp.domain.repository.AuthRepository

@Composable
fun QuizNavHost(
    authRepository: AuthRepository,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen Route
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val nextRoute = if (authRepository.getCurrentUser() != null) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen Route
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen Route
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onCategorySelected = { categoryId ->
                    navController.navigate(Screen.QuizPlay.createRoute(categoryId))
                },
                onStartDailyChallenge = { challengeId ->
                    navController.navigate(Screen.QuizPlay.createRoute(challengeId))
                },
                onQuickStartQuiz = {
                    navController.navigate(Screen.QuizPlay.createRoute("random_quick"))
                },
                onSignOutClick = {
                    homeViewModel.signOut {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Quiz Play Destination (Placeholder for full Quiz flow)
        composable(
            route = Screen.QuizPlay.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "default"
            QuizPlayPlaceholderScreen(
                categoryId = categoryId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun QuizPlayPlaceholderScreen(
    categoryId: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Quiz Game Ready!\nCategory ID: $categoryId",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
