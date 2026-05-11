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
    var finalScore by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf(listOf<String>()) }
    var totalQuizTime by remember { mutableIntStateOf(300) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Kotlin") }
    var selectedQuizTitle by remember { mutableStateOf("") }

    val sessionUser by loginManager.currentSession.collectAsState(initial = null)

    LaunchedEffect(sessionUser) {
        val session = sessionUser
        if (!session.isNullOrBlank()) {
            userName = session

            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "home") {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        //startDestination = "home"
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(

                onLoginClick = { name ->
                    userName = name.ifBlank { "Guest" }
                    navController.navigate("home")
                },

                onRegisterClick = {
                    navController.navigate("registration")
                }
            )
        }

        composable("registration") {
            RegistrationScreen(
                onBackClick = { navController.navigate("login") },
                onFinishClick = { navController.navigate("login") }
            )
        }

        composable("home") {
            HomePageScreen(
                username = userName,
                onOpenQuiz = { category, title ->
                    selectedCategory = category
                    selectedQuizTitle = title
                    navController.navigate("quizSetup")
                },
                onLogOut = {
                    // Navigate first to avoid a recomposition "flash" on Home with cleared state.
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }

                    scope.launch {
                        loginManager.logout()
                        userName = ""
                        selectedQuestions = emptyList()
                        finalScore = 0
                        elapsedSeconds = 0
                        userAnswers = emptyList()
                    }
                }
            )
        }

        composable("quizSetup") {
            QuizSetupScreen(
                quizTitle = selectedQuizTitle,

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

                onQuizComplete = { score, answers, elapsed ->
                    finalScore = score
                    userAnswers = answers
                    elapsedSeconds = elapsed
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
                questions = selectedQuestions,
                userAnswers = userAnswers,
                elapsedSeconds = elapsedSeconds,

                onRestartClick = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                    // Clear after navigation so ResultScreen doesn't briefly show 0/0.
                    finalScore = 0
                    elapsedSeconds = 0
                    userAnswers = emptyList()
                    selectedQuestions = emptyList()
                }
            )
        }
    }
}