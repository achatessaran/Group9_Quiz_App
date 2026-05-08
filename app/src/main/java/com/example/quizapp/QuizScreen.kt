package com.example.quizapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    questions: List<Question>,
    totalTimeInSeconds: Int,
    onQuizComplete: (Int, List<String>) -> Unit,
    onExitQuiz: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(listOf<String>()) }
    var timeLeft by remember { mutableStateOf(totalTimeInSeconds) }
    var isQuizFinished by remember { mutableStateOf(false) }

    val currentQuestion = questions[currentQuestionIndex]
    val shuffledOptions = remember(currentQuestionIndex) {
        currentQuestion.options.shuffled()
    }
    LaunchedEffect(timeLeft, isQuizFinished) {
        if (timeLeft > 0 && !isQuizFinished) {
            delay(1000)
            timeLeft--
        } else if (timeLeft == 0 && !isQuizFinished) {
            isQuizFinished = true
            onQuizComplete(score, userAnswers)
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Q ${currentQuestionIndex + 1}/${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "⏱ $formattedTime",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        timeLeft <= 30 -> MaterialTheme.colorScheme.error
                        timeLeft <= 60 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        LinearProgressIndicator(
            progress = {
                (currentQuestionIndex + 1).toFloat() / questions.size.toFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = currentQuestion.questionText,
                modifier = Modifier.padding(22.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        shuffledOptions.forEach { option ->
            AnswerOptionCard(
                optionText = option,
                selected = selectedAnswer == option,
                onClick = { selectedAnswer = option }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

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
                    isQuizFinished = true
                    onQuizComplete(score, userAnswers + selectedAnswer)
                }
            },
            enabled = selectedAnswer.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (currentQuestionIndex == questions.size - 1)
                    "Finish Quiz"
                else
                    "Next Question",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onExitQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Exit Quiz",
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}

@Composable
fun AnswerOptionCard(
    optionText: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 5.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = optionText,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}