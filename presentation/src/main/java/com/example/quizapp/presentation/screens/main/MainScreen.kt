package com.example.quizapp.presentation.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.presentation.screens.home.HomeScreen
import com.example.quizapp.presentation.screens.category.CategoryScreen
import com.example.quizapp.presentation.screens.addquiz.AddQuizScreen
import com.example.quizapp.presentation.screens.favorite.FavoriteScreen
import com.example.quizapp.presentation.screens.profile.ProfileScreen
import com.example.quizapp.presentation.viewmodels.HomeViewModel

sealed class Tab(val title: String, val icon: ImageVector) {
    object Home : Tab("Home", Icons.Default.Home)
    object Category : Tab("Category", Icons.Default.Category)
    object AddQuiz : Tab("Add Quiz", Icons.Default.AddBox)
    object Favorite : Tab("Favorite", Icons.Default.Favorite)
    object Profile : Tab("Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    viewModel: HomeViewModel,
    onCategorySelected: (String) -> Unit,
    onStartDailyChallenge: (String) -> Unit,
    onQuickStartQuiz: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf<Tab>(Tab.Home) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            CustomBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
//               .padding(innerPadding)
                .padding(bottom = 14.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Crossfade(
                targetState = selectedTab,
                animationSpec = tween(durationMillis = 200),
                label = "TabTransition"
            ) { tab ->
                when (tab) {
                    Tab.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onCategorySelected = onCategorySelected,
                            onStartDailyChallenge = onStartDailyChallenge,
                            onQuickStartQuiz = onQuickStartQuiz,
                            onSignOutClick = onSignOutClick
                        )
                    }
                    Tab.Category -> {
                        CategoryScreen(
                            categories = uiState.categories,
                            onCategorySelected = onCategorySelected
                        )
                    }
                    Tab.AddQuiz -> {
                        AddQuizScreen(
                            onQuizAdded = {
                                selectedTab = Tab.Home
                            }
                        )
                    }
                    Tab.Favorite -> {
                        FavoriteScreen(
                            onPlayCategory = onCategorySelected
                        )
                    }
                    Tab.Profile -> {
                        ProfileScreen(
                            stats = uiState.userStats,
                            onSignOutClick = onSignOutClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBottomNavigationBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(Tab.Home, Tab.Category, Tab.AddQuiz, Tab.Favorite, Tab.Profile)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab

                // Animate offset of selected tab icon to shift UP by 4dp
                val iconOffset by animateDpAsState(
                    targetValue = if (isSelected) (-4).dp else 0.dp,
                    animationSpec = tween(durationMillis = 150),
                    label = "IconOffsetAnimation"
                )

                // Animate colors
                val tintColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    animationSpec = tween(durationMillis = 150),
                    label = "ColorTintAnimation"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab) }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = tintColor,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = iconOffset)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = tintColor
                        )
                    )
                }
            }
        }
    }
}
