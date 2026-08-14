package com.example.quizapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.DailyChallenge
import com.example.quizapp.domain.model.QuizCategory
import com.example.quizapp.domain.model.UserStats
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.presentation.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
        loadHomeData()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { currentUser ->
                val username = currentUser?.displayName ?: currentUser?.email?.substringBefore("@") ?: "Explorer"
                val photoUrl = currentUser?.photoUrl
                _uiState.update { state ->
                    val updatedStats = state.userStats?.copy(
                        username = username,
                        avatarUrl = photoUrl
                    ) ?: UserStats(
                        username = username,
                        totalXp = 2450,
                        streakDays = 5,
                        quizzesCompleted = 18,
                        accuracyPercentage = 88,
                        level = 7,
                        avatarUrl = photoUrl
                    )
                    state.copy(userStats = updatedStats)
                }
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentUser = authRepository.getCurrentUser()
            val username = currentUser?.displayName ?: currentUser?.email?.substringBefore("@") ?: "Explorer"
            val photoUrl = currentUser?.photoUrl

            val mockUserStats = UserStats(
                username = username,
                totalXp = 2450,
                streakDays = 5,
                quizzesCompleted = 18,
                accuracyPercentage = 88,
                level = 7,
                avatarUrl = photoUrl
            )

            val mockDailyChallenge = DailyChallenge(
                id = "daily_01",
                title = "Cosmic Wonders & Black Holes",
                categoryName = "Science",
                xpReward = 150,
                totalQuestions = 10,
                isCompleted = false
            )

            val mockCategories = listOf(
                QuizCategory(
                    id = "cat_science",
                    title = "Science & Cosmos",
                    description = "Explore physics, astronomy, and nature mysteries.",
                    iconName = "science",
                    primaryColorHex = 0xFF00A8FF,
                    totalQuestions = 50,
                    difficulty = "Medium",
                    completedQuestions = 25,
                    progressPercentage = 0.50f
                ),
                QuizCategory(
                    id = "cat_tech",
                    title = "Technology & AI",
                    description = "Test your computer science and modern tech knowledge.",
                    iconName = "tech",
                    primaryColorHex = 0xFF38ADA9,
                    totalQuestions = 40,
                    difficulty = "Hard",
                    completedQuestions = 10,
                    progressPercentage = 0.25f
                ),
                QuizCategory(
                    id = "cat_history",
                    title = "World History",
                    description = "Journey through ancient empires and modern events.",
                    iconName = "history",
                    primaryColorHex = 0xFFE1B12C,
                    totalQuestions = 60,
                    difficulty = "Easy",
                    completedQuestions = 45,
                    progressPercentage = 0.75f
                ),
                QuizCategory(
                    id = "cat_art",
                    title = "Art & Literature",
                    description = "Famous paintings, classic books, and legendary authors.",
                    iconName = "art",
                    primaryColorHex = 0xFF9C88FF,
                    totalQuestions = 35,
                    difficulty = "Medium",
                    completedQuestions = 5,
                    progressPercentage = 0.14f
                ),
                QuizCategory(
                    id = "cat_sports",
                    title = "Sports & Fitness",
                    description = "Olympics, football, basketball, and legendary athletes.",
                    iconName = "sports",
                    primaryColorHex = 0xFF44BD32,
                    totalQuestions = 45,
                    difficulty = "Easy",
                    completedQuestions = 30,
                    progressPercentage = 0.66f
                ),
                QuizCategory(
                    id = "cat_geography",
                    title = "World Geography",
                    description = "Capitals, flags, landmarks, and ocean depths.",
                    iconName = "geography",
                    primaryColorHex = 0xFF00CEC9,
                    totalQuestions = 55,
                    difficulty = "Medium",
                    completedQuestions = 15,
                    progressPercentage = 0.27f
                )
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    userStats = mockUserStats,
                    dailyChallenge = mockDailyChallenge,
                    categories = mockCategories,
                    filteredCategories = mockCategories
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(searchQuery = query)
            applyFilters(updatedState)
        }
    }

    fun onFilterChipSelected(chip: String) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(selectedFilterChip = chip)
            applyFilters(updatedState)
        }
    }

    private fun applyFilters(state: HomeUiState): HomeUiState {
        val filtered = state.categories.filter { category ->
            val matchesQuery = state.searchQuery.isBlank() ||
                    category.title.contains(state.searchQuery, ignoreCase = true) ||
                    category.description.contains(state.searchQuery, ignoreCase = true)

            val matchesChip = state.selectedFilterChip == "All" ||
                    category.title.contains(state.selectedFilterChip, ignoreCase = true)

            matchesQuery && matchesChip
        }

        return state.copy(filteredCategories = filtered)
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onSignedOut()
        }
    }
}
