package com.example.quizapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    questions: List<Question>,
    totalTimeInSeconds: Int,
    onQuizComplete: (Int, List<String>) -> Unit,
    onExitQuiz: () -> Unit
) {
    require(questions.isNotEmpty()) { "QuizScreen requires at least one question" }

    var currentQuestionIndex by remember(questions) { mutableStateOf(0) }
    val userAnswers = remember(questions) {
        mutableStateListOf<String>().apply {
            repeat(questions.size) { add("") }
        }
    }
    val shuffledOptionsByQuestion = remember(questions) {
        questions.map { question ->
            when (question) {
                is MCQuestion -> question.options.shuffled()
                else -> emptyList()
            }
        }
    }
    var timeLeft by remember(questions, totalTimeInSeconds) { mutableStateOf(totalTimeInSeconds) }
    var isQuizFinished by remember(questions) { mutableStateOf(false) }

    fun finishQuiz() {
        val finalScore = questions.indices.count { index ->
            val question = questions[index]
            val answer = userAnswers.getOrNull(index).orEmpty()

            when (question) {
                is MCQuestion -> answer == question.correctAnswer
                is FITBQuestion -> answer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
                else -> false
            }
        }
        onQuizComplete(finalScore, userAnswers.toList())
    }



    val currentQuestion = questions[currentQuestionIndex]

    LaunchedEffect(timeLeft, isQuizFinished) {
        if (timeLeft > 0 && !isQuizFinished) {
            delay(1000)
            timeLeft--
        } else if (timeLeft == 0 && !isQuizFinished) {
            isQuizFinished = true
            finishQuiz()
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    val response = userAnswers[currentQuestionIndex]
    val options = shuffledOptionsByQuestion[currentQuestionIndex]
    val openAlertDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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
                },
                navigationIcon = {
                    IconButton(onClick = { openAlertDialog.value = !openAlertDialog.value }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = ((currentQuestionIndex == questions.size - 1) && userAnswers[currentQuestionIndex].isNotBlank()),
                        onClick = {
                            isQuizFinished = true
                            finishQuiz()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Finished"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        }
                    },
                    enabled = currentQuestionIndex > 0,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(72.dp)
                ) {
                    //Text(
                        //text = "P",
                        //style = MaterialTheme.typography.titleMedium
                    //)
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                        contentDescription = "PrevQuestion"
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                FilledIconButton(
                    onClick = {
                        currentQuestionIndex++
                    },
                    enabled = (userAnswers[currentQuestionIndex].isNotBlank() &&
                            (currentQuestionIndex < questions.size - 1)),
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(72.dp)
                ) {
                    //Text(
                        //text = "N",
                        //style = MaterialTheme.typography.titleMedium
                    //)
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = "NextQuestion"
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(24.dp)
        ) {
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

            Text(
                text = "Q ${currentQuestionIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(18.dp))

            // The question+answers area fills the remaining height above the bottomBar,
            // allowing options/fields to stick near the bottom without covering the buttons.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    when (currentQuestion) {
                        is MCQuestion -> {
                            MCQ(
                                questionText = currentQuestion.questionText,
                                options = options,
                                selectedOption = response,
                                onOptionSelected = { option -> userAnswers[currentQuestionIndex] = option }
                            )
                        }

                        is FITBQuestion -> {
                            FITB(
                                questionText = currentQuestion.questionText,
                                answer = response,
                                onAnswerChange = { text -> userAnswers[currentQuestionIndex] = text }
                            )
                        }

                        else -> {
                            // TODO: to be removed after populating with other types of question
                            TempOtherQuestion(questionText = currentQuestion.questionText)
                        }
                    }
                }
            }

            when {
                // [END_EXCLUDE]
                openAlertDialog.value -> {
                    ConfirmExitDialog(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            openAlertDialog.value = false
                            onExitQuiz()
                        },
                        dialogTitle = "Quitting Now?",
                        dialogText = "Your progress will not be saved."
                        //icon = Icons.Default.Info
                    )
                }
            }

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
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(
            0.dp
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

@Composable
fun MCQ(
    questionText: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = questionText,
                modifier = Modifier.padding(22.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        options.forEach { option ->
            AnswerOptionCard(
                optionText = option,
                selected = selectedOption == option,
                onClick = {
                    onOptionSelected(option)
                }
            )
        }
    }
}

@Composable
fun FITB(
    questionText: String,
    answer: String,
    onAnswerChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize())
    {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = answer,
            onValueChange = onAnswerChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your answer") }
        )
    }
}

@Composable
fun TempOtherQuestion(questionText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = questionText,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "This question type isn't supported yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ConfirmExitDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    androidx.compose.material3.AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Dismiss")
            }
        }
    )
}