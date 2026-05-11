package com.example.quizapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreenOld(
    userName: String,
    score: Int,
    totalQuestions: Int,
    onRestartClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    val percentage = (score.toFloat() / totalQuestions.toFloat()) * 100

    val message = when {
        percentage >= 80 -> "Excellent work, $userName!"
        percentage >= 60 -> "Good job, $userName!"
        else -> "Keep practicing, $userName!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = message)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Score: $score / $totalQuestions",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Percentage: ${percentage.toInt()}%",
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onReviewClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Review Answers")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Restart Quiz")
        }
    }
}