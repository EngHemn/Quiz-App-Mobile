package com.example.quizapp.domain.model

data class QuizCategory(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val primaryColorHex: Long,
    val totalQuestions: Int,
    val difficulty: String, // e.g. "Easy", "Medium", "Hard"
    val completedQuestions: Int = 0,
    val progressPercentage: Float = 0f
)
