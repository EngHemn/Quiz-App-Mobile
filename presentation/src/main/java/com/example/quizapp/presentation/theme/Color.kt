package com.example.quizapp.presentation.theme

import androidx.compose.ui.graphics.Color

// Light Theme Colors from Design System
val LightPrimary = Color(0xFF7B561A)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFF9DDB8)
val LightOnPrimaryContainer = Color(0xFF271903)

val LightSecondary = Color(0xFF6C5C43)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFF6DFC0)
val LightOnSecondaryContainer = Color(0xFF251A07)

val LightTertiary = Color(0xFF486727)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFC8EF9C)
val LightOnTertiaryContainer = Color(0xFF112004)

val LightError = Color(0xFFAB2D25)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFF9DBD7)
val LightOnErrorContainer = Color(0xFF3B0605)

val LightBackground = Color(0xFFFEFBFF)
val LightOnBackground = Color(0xFF1E1B17)
val LightSurface = Color(0xFFFEFBFF)
val LightOnSurface = Color(0xFF1E1B17)

val LightOutline = Color(0xFF7F7569)
val LightSurfaceVariant = Color(0xFFEDE1D1)
val LightOnSurfaceVariant = Color(0xFF4D453A)

// Keeping the old color variable names to prevent compilation errors in any other file references
val PrimaryPurple = LightPrimary
val PrimaryPurpleLight = LightPrimaryContainer
val SecondaryCyan = LightSecondary
val SecondaryCyanLight = LightSecondaryContainer
val AccentOrange = LightTertiary
val AccentOrangeFlame = LightError
val AccentGold = Color(0xFFFDCB6E) // Keep gold/yellow for XP/Badges

val DarkBackground = LightBackground
val DarkSurface = LightSurface
val DarkSurfaceCard = LightSurfaceVariant
val DarkSurfaceBorder = LightOutline

val TextPrimaryDark = LightOnBackground
val TextSecondaryDark = LightOnSurfaceVariant
val TextPrimaryLight = LightOnBackground
val TextSecondaryLight = LightOnSurfaceVariant

// Quiz Category Colors
val CategoryScience = Color(0xFF00A8FF)
val CategoryHistory = Color(0xFFE1B12C)
val CategoryArt = Color(0xFF9C88FF)
val CategorySports = Color(0xFF44BD32)
val CategoryPopCulture = Color(0xFFE84393)
val CategoryGeography = Color(0xFF00CEC9)
val CategoryTech = Color(0xFF38ADA9)
