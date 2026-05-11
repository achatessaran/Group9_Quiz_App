package com.example.quizapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    userName: String,
    score: Int,
    questions:  List<Question>,
    userAnswers: List<String>,
    elapsedSeconds: Int,
    onRestartClick: () -> Unit
) {
    // Avoid a one-frame flash with empty values if upstream state clears data during logout.
    // Keep showing the last non-blank results until this screen is disposed.
    val stableUserName = remember { userName }
    val stableScore = remember { score }
    val stableQuestions = remember { questions }
    val stableUserAnswers = remember { userAnswers }
    val stableElapsedSeconds = remember { elapsedSeconds }

    var showDetails by rememberSaveable { mutableStateOf(false) }
    val resultSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val totalQuestions = stableQuestions.size
    val percentage = if (totalQuestions > 0) {
        (stableScore.toFloat() / totalQuestions.toFloat()) * 100
    } else {
        0f
    }

    val message = when {
        percentage >= 80 -> "Excellent work, $stableUserName!"
        percentage >= 60 -> "Good job, $stableUserName!"
        else -> "Keep practicing, $stableUserName!"
    }

    val emoji = when {
        percentage >= 80 -> "🏆"
        percentage >= 60 -> "👍"
        else -> "📘"
    }

    val elapsedMinutes = stableElapsedSeconds / 60
    val elapsedRemainderSeconds = stableElapsedSeconds % 60
    val formattedElapsed = String.format(
        Locale.getDefault(),
        "%02d:%02d",
        elapsedMinutes,
        elapsedRemainderSeconds
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 120.dp, start = 24.dp, bottom = 120.dp, end = 24.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Completed",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "$stableScore / $totalQuestions",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Score",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "${percentage.toInt()}%",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Percentage",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = formattedElapsed,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Time Taken",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(96.dp))

        Button(
            //onClick = onReviewClick,
            onClick = { showDetails = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Review Answers",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRestartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Finish",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    if (showDetails) {
        ModalBottomSheet(
            onDismissRequest = { showDetails = false },
            sheetState = resultSheetState,
        ) {
            ReviewScreen(
                questions = stableQuestions,
                userAnswers = stableUserAnswers
            )
        }
    }
}