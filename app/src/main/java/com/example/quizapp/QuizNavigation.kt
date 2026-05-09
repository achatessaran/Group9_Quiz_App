package com.example.quizapp

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun QuizNavigation() {

    val navController = rememberNavController()

    var userName by remember { mutableStateOf("") }
    var selectedQuestions by remember { mutableStateOf(listOf<Question>()) }
    var finalScore by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(listOf<String>()) }
    var totalQuizTime by remember { mutableStateOf(300) }

    NavHost(
        navController = navController,
        //startDestination = "home"
        startDestination = "nameInput"
    ) {

        /**  old landing page
        composable("home") {
            HomeScreen(
                onStartClick = {
                    navController.navigate("nameInput")
                }
            )
        }
        **/

        composable("home") {
            HomePageScreen(
                username = userName,
                onOpenQuiz = {
                    navController.navigate("quizSetup")
                },
                onLogOut = {
                    navController.navigate("nameInput")}
            )
        }

        composable("nameInput") {
            NameInputScreen(

                onContinueClick = { name ->
                    userName = name.ifBlank { "Guest" }
                    navController.navigate("home")
                },

                //onBackClick = {
                    //navController.navigate("home")
                //}
            )
        }

        composable("quizSetup") {
            QuizSetupScreen(

                onStartQuiz = { difficulty, count ->
                    // TODO: add "category" or something similars
                    // so that users can switch between different datasets.
                    selectedQuestions = quizQuestions
                        .filter { it.difficulty == difficulty }
                        .shuffled()
                        .take(count)

                    val minutesPerFiveQuestions = when (difficulty) {
                        "Easy" -> 5
                        "Medium" -> 7
                        else -> 10
                    }

                    val totalMinutes =
                        (count / 5) * minutesPerFiveQuestions

                    totalQuizTime = totalMinutes * 60

                    navController.navigate("quiz")
                },

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }


        composable("quiz") {
            QuizScreen(
                questions = selectedQuestions,
                totalTimeInSeconds = totalQuizTime,

                onQuizComplete = { score, answers ->
                    finalScore = score
                    userAnswers = answers
                    navController.navigate("result")
                },

                onExitQuiz = {
                    navController.navigate("home")
                }
            )
        }

        composable("result") {
            ResultScreen(
                userName = userName,
                score = finalScore,
                totalQuestions = selectedQuestions.size,

                onRestartClick = {
                    finalScore = 0
                    userAnswers = emptyList()
                    selectedQuestions = emptyList()

                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },

                onReviewClick = {
                    navController.navigate("review")
                }
            )
        }

        composable("review") {
            ReviewScreen(
                questions = selectedQuestions,
                userAnswers = userAnswers,

                onBackToResult = {
                    navController.navigate("result")
                }
            )
        }
    }
}