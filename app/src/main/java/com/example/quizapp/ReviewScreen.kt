package com.example.quizapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun ReviewScreen(
    questions: List<Question>,
    userAnswers: List<String>,
    onBackToResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        questions.forEachIndexed { index, question ->

            val userAnswer = userAnswers.getOrElse(index) { "No answer" }
            val isCorrect = when (question) {
                is MCQuestion -> userAnswer == question.correctAnswer
                is FITBQuestion -> userAnswer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
                else -> false
            }

            QuestionCard(isCorrect, question, index, userAnswer, question.correctAnswer)
        }

        /**
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToResult,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Back to Result",
                style = MaterialTheme.typography.titleMedium
            )
        } **/
    }
}

@Composable
fun QuestionCard(
    isCorrect: Boolean,
    question: Question,
    index: Int,
    userAnswer: String,
    correctAnswer: String
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect)
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f)
            else
                MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isCorrect)
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.50f)
            else
                MaterialTheme.colorScheme.error.copy(alpha = 0.50f)
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${index + 1}. ${question.questionText}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your Answer: $userAnswer",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Correct Answer: $correctAnswer",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                /**
                Text(
                    text = if (isCorrect) "✓ Correct" else "✗ Wrong",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.error
                ) **/
            }
        }
    }
}