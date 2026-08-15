package com.example.quizapp.presentation.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.domain.model.QuizCategory
import com.example.quizapp.presentation.components.AppTopBarComponent
import com.example.quizapp.presentation.theme.AccentGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categories: List<QuizCategory>,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("All") }

    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    val filteredCategories = remember(categories, searchQuery, selectedDifficulty) {
        categories.filter { category ->
            val matchesSearch = category.title.contains(searchQuery, ignoreCase = true) ||
                    category.description.contains(searchQuery, ignoreCase = true)
            val matchesDifficulty = selectedDifficulty == "All" ||
                    category.difficulty.equals(selectedDifficulty, ignoreCase = true)
            matchesSearch && matchesDifficulty
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBarComponent(
                title = "Categories",
                subtitle = "Choose your favorite topic to start a quiz"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search categories...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Difficulty filter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            difficulties.forEach { difficulty ->
                val isSelected = selectedDifficulty == difficulty
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDifficulty = difficulty },
                    label = { Text(text = difficulty) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color.Transparent,
                        selectedBorderColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid
        if (filteredCategories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No categories found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCategories, key = { it.id }) { category ->
                    val categoryIcon: ImageVector = when (category.iconName.lowercase()) {
                        "science" -> Icons.Default.Psychology
                        "tech" -> Icons.Default.Computer
                        "history" -> Icons.Default.HistoryEdu
                        "art" -> Icons.Default.MenuBook
                        "sports" -> Icons.Default.SportsSoccer
                        "geography" -> Icons.Default.Public
                        else -> Icons.Default.AutoAwesome
                    }
                    val cardColor = Color(category.primaryColorHex)

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category.id) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = cardColor.copy(alpha = 0.2f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = categoryIcon,
                                        contentDescription = category.title,
                                        tint = cardColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = category.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                ),
                                maxLines = 2,
                                modifier = Modifier.height(32.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${category.totalQuestions} Qs",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (category.difficulty.lowercase()) {
                                        "easy" -> Color(0xFF44BD32).copy(alpha = 0.15f)
                                        "hard" -> Color(0xFFFF7675).copy(alpha = 0.15f)
                                        else -> AccentGold.copy(alpha = 0.15f)
                                    }
                                ) {
                                    Text(
                                        text = category.difficulty,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = when (category.difficulty.lowercase()) {
                                                "easy" -> Color(0xFF44BD32)
                                                "hard" -> Color(0xFFFF7675)
                                                else -> AccentGold
                                            }
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}

