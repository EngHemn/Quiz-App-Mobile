package com.example.quizapp.presentation.screens.login

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.presentation.uistate.LoginUiState
import com.example.quizapp.presentation.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Trigger navigation on success
    LaunchedEffect(uiState.user) {
        if (uiState.user != null) {
            onLoginSuccess()
        }
    }

    // Configure Google Sign-In options
    val gso = remember {
        val webClientId = getDefaultWebClientId(context)
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleGoogleSignInResult(result.data)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant Quiz Logo / Icon
            QuizLogo()

            Spacer(modifier = Modifier.height(32.dp))

            // Greeting & Description Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to QuizApp",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Test your knowledge, compete with friends, and earn daily rewards. Sign in with Google to save your progress and access categories.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Premium Styled Google Sign In Button
            Button(
                onClick = {
                    // Clear any previous error and trigger sign-in intent
                    viewModel.clearError()
                    launcher.launch(googleSignInClient.signInIntent)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    disabledContentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Signing in...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    } else {
                        GmailIcon(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Sign in with Gmail",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // Error display
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.error?.let { error ->
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizLogo() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Text(
            text = "?",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 48.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun GoogleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Red part
        val redPath = Path().apply {
            moveTo(width * 0.5f, height * 0.5f)
            lineTo(width * 0.95f, height * 0.35f)
            cubicTo(
                width * 0.9f, height * 0.15f,
                width * 0.72f, height * 0.05f,
                width * 0.5f, height * 0.05f
            )
            cubicTo(
                width * 0.28f, height * 0.05f,
                width * 0.1f, height * 0.18f,
                width * 0.05f, height * 0.35f
            )
            lineTo(width * 0.25f, height * 0.5f)
            close()
        }
        drawPath(redPath, color = Color(0xFFEA4335))

        // Yellow part
        val yellowPath = Path().apply {
            moveTo(width * 0.5f, height * 0.5f)
            lineTo(width * 0.25f, height * 0.5f)
            cubicTo(
                width * 0.25f, height * 0.65f,
                width * 0.32f, height * 0.78f,
                width * 0.45f, height * 0.85f
            )
            lineTo(width * 0.68f, height * 0.68f)
            close()
        }
        drawPath(yellowPath, color = Color(0xFFFBBC05))

        // Green part
        val greenPath = Path().apply {
            moveTo(width * 0.5f, height * 0.5f)
            lineTo(width * 0.68f, height * 0.68f)
            cubicTo(
                width * 0.63f, height * 0.75f,
                width * 0.57f, height * 0.85f,
                width * 0.5f, height * 0.85f
            )
            cubicTo(
                width * 0.32f, height * 0.85f,
                width * 0.15f, height * 0.75f,
                width * 0.05f, height * 0.5f
            )
            lineTo(width * 0.25f, height * 0.5f)
            close()
        }
        drawPath(greenPath, color = Color(0xFF34A853))

        // Blue part
        val bluePath = Path().apply {
            moveTo(width * 0.5f, height * 0.5f)
            lineTo(width * 0.95f, height * 0.5f)
            cubicTo(
                width * 0.95f, height * 0.45f,
                width * 0.95f, height * 0.4f,
                width * 0.92f, height * 0.35f
            )
            lineTo(width * 0.5f, height * 0.5f)
            close()
        }
        drawPath(bluePath, color = Color(0xFF4285F4))
    }
}

private fun getDefaultWebClientId(context: Context): String {
    val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
    return if (resId != 0) context.getString(resId) else "YOUR_WEB_CLIENT_ID"
}

@Composable
fun GmailIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Left Vertical Stripe (Blue)
        val leftPath = Path().apply {
            moveTo(0f, h * 0.25f)
            lineTo(w * 0.22f, h * 0.25f)
            lineTo(w * 0.22f, h)
            lineTo(0f, h)
            close()
        }
        drawPath(leftPath, color = Color(0xFF4285F4))

        // 2. Right Vertical Stripe (Green)
        val rightPath = Path().apply {
            moveTo(w * 0.78f, h * 0.25f)
            lineTo(w, h * 0.25f)
            lineTo(w, h)
            lineTo(w * 0.78f, h)
            close()
        }
        drawPath(rightPath, color = Color(0xFF34A853))

        // 3. Middle V shape & Left Arch (Red)
        val redPath = Path().apply {
            moveTo(0f, h * 0.25f)
            lineTo(w * 0.5f, h * 0.68f)
            lineTo(w * 0.78f, h * 0.44f)
            lineTo(w * 0.78f, h * 0.25f)
            lineTo(w * 0.5f, h * 0.5f)
            lineTo(0f, h * 0.25f)
            close()
        }
        drawPath(redPath, color = Color(0xFFEA4335))

        // 4. Yellow Top-Right Shoulder (Yellow)
        val yellowPath = Path().apply {
            moveTo(w * 0.78f, h * 0.44f)
            lineTo(w, h * 0.25f)
            lineTo(w * 0.78f, h * 0.25f)
            close()
        }
        drawPath(yellowPath, color = Color(0xFFFBBC05))
    }
}
