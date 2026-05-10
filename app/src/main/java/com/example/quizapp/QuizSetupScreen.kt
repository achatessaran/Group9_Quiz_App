package com.example.quizapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSetupScreen(
    quizTitle: String,
    onStartQuiz: (String, Int, Int) -> Unit,
    onBackClick: () -> Unit
) {
    var difficultiesExpanded by remember { mutableStateOf(false) }
    var countExpanded by remember { mutableStateOf(false) }
    val difficulties = listOf("Easy", "Medium", "Hard")
    val questionCount = listOf(5, 10, 15)
    var selectedDifficulty by remember { mutableStateOf(difficulties[0]) }
    var selectedQuestionCount by remember { mutableIntStateOf(questionCount[0]) }

    val totalQuizTime = quizTime(selectedDifficulty, selectedQuestionCount)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 120.dp, start = 24.dp, bottom = 120.dp, end = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(padding))
            Text(
                text = quizTitle.ifBlank { "Quiz Setup" },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minutes = totalQuizTime / 60
            val seconds = totalQuizTime % 60
            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            Text(
                text = "Time Limit: $formattedTime",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))
            ExposedDropdownMenuBox(
                expanded = difficultiesExpanded,
                onExpandedChange = { difficultiesExpanded = !difficultiesExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedDifficulty,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultiesExpanded) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = difficultiesExpanded,
                    onDismissRequest = { difficultiesExpanded = false }
                ) {
                    difficulties.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedDifficulty = item
                                difficultiesExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = countExpanded,
                onExpandedChange = { countExpanded = !countExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedQuestionCount.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countExpanded) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = countExpanded,
                    onDismissRequest = { countExpanded = false }
                ) {
                    questionCount.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.toString()) },
                            onClick = {
                                selectedQuestionCount = item
                                countExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    onStartQuiz(selectedDifficulty,
                        selectedQuestionCount,
                        totalQuizTime)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Start",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

fun quizTime(difficulty: String, questionNumber: Int): Int {
    val minutesPerFiveQuestions = when (difficulty) {
        "Easy" -> 5
        "Medium" -> 7
        else -> 10
    }

    val totalMinutes =
        (questionNumber / 5) * minutesPerFiveQuestions

    return totalMinutes * 60
}