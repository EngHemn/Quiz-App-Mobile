package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): AuthUser?
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>
    suspend fun signOut(): Result<Unit>
    fun observeAuthState(): Flow<AuthUser?>
}
