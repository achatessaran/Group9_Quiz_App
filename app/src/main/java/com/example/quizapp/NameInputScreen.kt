package com.example.quizapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// TODO: rename to login screen or something more accurate
@Composable
fun NameInputScreen(
    onContinueClick: (String) -> Unit,
    //onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val loginManager = remember(context) { LoginManager(context) }

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loginError by remember { mutableStateOf<String?>(null) }

    val registeredData by loginManager.verifyUser.collectAsState(initial = Pair("", ""))
    val (regUser, regPass) = registeredData

    val loginCompleted = userName.trim().isNotEmpty() && password.isNotBlank()
    val loginSuccessful = userName.trim() == regUser && password == regPass

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Distaste2Learn",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Light,
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            //shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                Text(
                    text = "Enter your name to personalize your quiz result.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))
                **/

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text(text = "Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                loginError = null
                if (loginSuccessful) {
                    val trimmedUser = userName.trim()
                    scope.launch { loginManager.startSession(trimmedUser) }
                    onContinueClick(trimmedUser)
                } else {
                    loginError = "Invalid username or password"
                }
            },
            enabled = loginCompleted,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (loginError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                // in a coroutine
                scope.launch {
                    loginManager.registerUser(userName.trim(), password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}