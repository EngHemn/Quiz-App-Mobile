package com.example.quizapp.domain.model

data class DailyChallenge(
    val id: String,
    val title: String,
    val categoryName: String,
    val xpReward: Int,
    val totalQuestions: Int,
    val isCompleted: Boolean = false
)
