package com.example.quizapp.presentation.uistate

import com.example.quizapp.domain.model.AuthUser

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = "Google Sign-In failed: Developer Error (Status 10). Make sure SHA-1 is added in Firebase.",
    val user: AuthUser? = null
)
