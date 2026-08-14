package com.example.quizapp.presentation.uistate

import com.example.quizapp.domain.model.DailyChallenge
import com.example.quizapp.domain.model.QuizCategory
import com.example.quizapp.domain.model.UserStats

data class HomeUiState(
    val searchQuery: String = "",
    val selectedFilterChip: String = "All",
    val availableFilterChips: List<String> = listOf("All", "Science", "Tech", "History", "Art", "Sports", "Geography"),
    val categories: List<QuizCategory> = emptyList(),
    val filteredCategories: List<QuizCategory> = emptyList(),
    val userStats: UserStats? = null,
    val dailyChallenge: DailyChallenge? = null,
    val isLoading: Boolean = true,
    val errorMsg: String? = null
)
