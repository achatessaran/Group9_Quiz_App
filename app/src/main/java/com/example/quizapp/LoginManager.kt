package com.example.quizapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

private val Context.registryStore by preferencesDataStore(name = "user_registry")
private val Context.sessionStore by preferencesDataStore(name = "login_session")

private val USERNAME_KEY = stringPreferencesKey("username")
private val PASSWORD_KEY = stringPreferencesKey("password")
private val SESSION_USER = stringPreferencesKey("session_user")

class LoginManager(private val context: Context) {
    private val dbFile = "accounts.txt"

    fun initialize() {
        val file = File(context.filesDir, dbFile)
        // create the admin if the file doesn't exist yet
        if (!file.exists()) {
            registerUser("admin", "12345")
            println("Admin account created for the first time.")
        }
    }
    // registration (simple single-account store)
    fun registerUser(user: String, pass: String): Boolean {
        if (verifyUser(user, pass)) return false
        val line = "$user,$pass\n"
        context.openFileOutput(dbFile, Context.MODE_APPEND).use {
            it.write(line.toByteArray())
        }
        return true
    }

    // login (reads the currently registered credentials)
    /**
    val verifyUser: Flow<Pair<String, String>> = context.registryStore.data.map {
        Pair(it[USERNAME_KEY] ?: "", it[PASSWORD_KEY] ?: "")
    }
    **/
    fun verifyUser(user: String, pass: String): Boolean {
        val file = File(context.filesDir, dbFile)
        if (!file.exists()) return false
        // simply compare with any login stored in the "database" nothing fancy
        return file.bufferedReader().useLines { lines ->
            lines.any { line ->
                val login = line.split(',')
                (login.size == 2 && login[0] == user && login[1] == pass)
            }
        }
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
