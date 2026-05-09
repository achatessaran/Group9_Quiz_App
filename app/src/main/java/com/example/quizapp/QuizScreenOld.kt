package com.example.quizapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreenOld(
    questions: List<MCQuestion>,
    onQuizComplete: (Int, List<String>) -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(listOf<String>()) }

    val currentQuestion = questions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        LinearProgressIndicator(
            progress = {
                (currentQuestionIndex + 1).toFloat() / questions.size.toFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp)
        )

        Text(
            text = currentQuestion.questionText,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        currentQuestion.options.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(
                        width = 2.dp,
                        color = if (selectedAnswer == option)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        selectedAnswer = option
                    }
            ) {
                Text(
                    text = option,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedAnswer == currentQuestion.correctAnswer) {
                    score++
                }

                userAnswers = userAnswers + selectedAnswer

                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    selectedAnswer = ""
                } else {
                    onQuizComplete(score, userAnswers + selectedAnswer)
                }
            },
            enabled = selectedAnswer.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (currentQuestionIndex == questions.size - 1)
                    "Finish Quiz"
                else
                    "Next"
            )
        }
    }
}