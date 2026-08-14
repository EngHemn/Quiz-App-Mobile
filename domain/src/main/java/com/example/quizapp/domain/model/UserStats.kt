package com.example.quizapp.domain.model

data class UserStats(
    val username: String,
    val totalXp: Int,
    val streakDays: Int,
    val quizzesCompleted: Int,
    val accuracyPercentage: Int,
    val level: Int,
    val avatarUrl: String? = null
)
