package com.example.quizapp

import android.R.attr.enabled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuBoxScope.menuAnchor
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var selectedQuestionCount by remember { mutableStateOf(questionCount[0]) }

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
                .padding(top = 120.dp, start = 24.dp, bottom = 60.dp, end = 24.dp),
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
                text = "Time Limit: ${formattedTime}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            //Spacer(modifier = Modifier.height(60.dp))
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
            Spacer(modifier = Modifier.height(96.dp))
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

@Composable
fun OldQuizSetupScreen(
    onStartQuiz: (String, Int) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    var selectedQuestionCount by remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Quiz Setup",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        listOf("Easy", "Medium", "Hard").forEach { difficulty ->
            SelectionCard(
                text = difficulty,
                selected = selectedDifficulty == difficulty,
                onClick = { selectedDifficulty = difficulty }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Number of Questions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        listOf(5, 10, 15).forEach { count ->
            SelectionCard(
                text = "$count Questions",
                selected = selectedQuestionCount == count,
                onClick = { selectedQuestionCount = count }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onStartQuiz(selectedDifficulty, selectedQuestionCount)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Start Quiz",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Back to Home",
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}

@Composable
fun SelectionCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 6.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}