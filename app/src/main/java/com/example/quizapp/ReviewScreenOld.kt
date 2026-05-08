package com.example.quizapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewScreenOld(
    questions: List<Question>,
    userAnswers: List<String>,
    onBackToResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Review Answers",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        questions.forEachIndexed { index, question ->

            val userAnswer = userAnswers.getOrElse(index) { "No answer" }
            val isCorrect = userAnswer == question.correctAnswer

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Q${index + 1}. ${question.questionText}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Your Answer: $userAnswer")
                    Text(text = "Correct Answer: ${question.correctAnswer}")
                    Text(
                        text = if (isCorrect) "Result: Correct" else "Result: Wrong",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Back to Result")
        }
    }
}