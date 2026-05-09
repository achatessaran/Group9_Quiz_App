package com.example.quizapp

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun QuizNavigation() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loginManager = remember(context) { LoginManager(context) }

    val navController = rememberNavController()

    var userName by remember { mutableStateOf("") }
    var selectedQuestions by remember { mutableStateOf(listOf<Question>()) }
    var finalScore by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(listOf<String>()) }
    var totalQuizTime by remember { mutableStateOf(300) }
    var selectedCategory by remember { mutableStateOf("Kotlin") }

    val sessionUser by loginManager.currentSession.collectAsState(initial = null)

    LaunchedEffect(sessionUser) {
        val session = sessionUser
        if (!session.isNullOrBlank()) {
            userName = session

            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "home") {
                navController.navigate("home") {
                    popUpTo("nameInput") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

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
                onOpenQuiz = { category ->
                    selectedCategory = category
                    navController.navigate("quizSetup")
                },
                onLogOut = {
                    scope.launch {
                        loginManager.logout()

                        userName = ""
                        selectedQuestions = emptyList()
                        finalScore = 0
                        userAnswers = emptyList()

                        navController.navigate("nameInput") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
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

                onStartQuiz = { difficulty, count, time ->
                    // TODO: add "category" or something similars
                    // so that users can switch between different datasets.
                    selectedQuestions = quizQuestions
                        .filter {
                            it.difficulty == difficulty &&
                                    it.category == selectedCategory
                        }
                        .shuffled()
                        .take(count)

                    totalQuizTime = time

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