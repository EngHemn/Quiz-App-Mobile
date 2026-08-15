package com.example.quizapp.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.presentation.components.AppTopBarComponent
import com.example.quizapp.presentation.theme.AccentGold
import com.example.quizapp.presentation.theme.AccentOrangeFlame
import com.example.quizapp.domain.model.UserStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    stats: UserStats?,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBarComponent(
                title = "Profile",
                subtitle = "View statistics and account details",
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

        // Avatar and Profile Info
        if (!stats?.avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = stats.avatarUrl,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stats?.username?.firstOrNull()?.toString() ?: "U",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stats?.username ?: "Explorer",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )

        Text(
            text = "Level ${stats?.level ?: 1} • Quiz Master",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Total XP",
                value = "${stats?.totalXp ?: 0}",
                icon = Icons.Default.Star,
                iconColor = AccentGold,
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "Daily Streak",
                value = "${stats?.streakDays ?: 0} Days",
                icon = Icons.Default.Whatshot,
                iconColor = AccentOrangeFlame,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Accuracy",
                value = "${stats?.accuracyPercentage ?: 0}%",
                icon = Icons.Default.TrendingUp,
                iconColor = Color(0xFF00CEC9),
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "Quizzes Done",
                value = "${stats?.quizzesCompleted ?: 0}",
                icon = Icons.Default.Quiz,
                iconColor = Color(0xFF9C88FF),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Settings / Options
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Account Settings",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                SettingsItem(
                    title = "Personal Information",
                    icon = Icons.Default.Person,
                    onClick = {}
                )
                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f))

                SettingsItem(
                    title = "Achievements",
                    icon = Icons.Default.EmojiEvents,
                    onClick = {}
                )
                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f))

                SettingsItem(
                    title = "Notifications",
                    icon = Icons.Default.Notifications,
                    onClick = {}
                )
                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f))

                SettingsItem(
                    title = "Log Out",
                    icon = Icons.Default.ExitToApp,
                    tint = MaterialTheme.colorScheme.error,
                    onClick = onSignOutClick
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
}

@Composable
private fun StatsCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    icon: ImageVector,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = tint
                )
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}
