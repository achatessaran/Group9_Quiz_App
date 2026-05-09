package com.example.quizapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.registryStore by preferencesDataStore(name = "user_registry")
private val Context.sessionStore by preferencesDataStore(name = "login_session")

private val USERNAME_KEY = stringPreferencesKey("username")
private val PASSWORD_KEY = stringPreferencesKey("password")
private val SESSION_USER = stringPreferencesKey("session_user")

class LoginManager(private val context: Context) {
    // registration (simple single-account store)
    suspend fun registerUser(username: String, password: String) {
        context.registryStore.edit {
            it[USERNAME_KEY] = username
            it[PASSWORD_KEY] = password
        }
    }

    // login (reads the currently registered credentials)
    val verifyUser: Flow<Pair<String, String>> = context.registryStore.data.map {
        Pair(it[USERNAME_KEY] ?: "", it[PASSWORD_KEY] ?: "")
    }

    // logics of user per session

    suspend fun startSession(user: String) {
        context.sessionStore.edit { it[SESSION_USER] = user }
    }

    // who is it?
    val currentSession: Flow<String?> = context.sessionStore.data.map { it[SESSION_USER] }

    suspend fun logout() {
        context.sessionStore.edit { it.clear() }
    }
}
