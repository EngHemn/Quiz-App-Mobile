package com.example.quizapp.presentation.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.presentation.uistate.LoginUiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun handleGoogleSignInResult(intent: Intent?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    signInWithGoogle(idToken)
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Google Sign-In returned empty ID Token.") }
                }
            } catch (e: ApiException) {
                val errorMessage = when (e.statusCode) {
                    12501 -> "Google Sign-In flow cancelled."
                    12500 -> "Google Sign-In failed: Configuration issue (Status 12500)."
                    10 -> "Google Sign-In failed: Developer Error (Status 10). Make sure SHA-1 is added in Firebase."
                    7 -> "Google Sign-In failed: Network Error (Status 7)."
                    else -> "Google Sign-In failed: ${e.localizedMessage ?: "Code ${e.statusCode}"}"
                }
                _uiState.update { it.copy(isLoading = false, error = errorMessage) }
            }
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = { user ->
                    _uiState.update { it.copy(isLoading = false, user = user) }
                },
                onFailure = { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.localizedMessage ?: "Unknown authentication error") }
                }
            )
        }
    }

    fun onAuthError(errorMsg: String) {
        _uiState.update { it.copy(error = errorMsg) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
