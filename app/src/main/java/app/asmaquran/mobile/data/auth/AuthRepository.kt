package app.asmaquran.mobile.data.auth

import android.app.Activity

interface AuthRepository {
    suspend fun hasActiveSession(): Boolean
    suspend fun isAuthPromptCompleted(): Boolean
    suspend fun markAuthPromptCompleted()
    suspend fun signInWithGoogle(activity: Activity): AuthSignInResult
}

sealed interface AuthSignInResult {
    data object Success : AuthSignInResult
    data object Cancelled : AuthSignInResult
    data object NotConfigured : AuthSignInResult
    data object NoToken : AuthSignInResult
    data class Failure(val throwable: Throwable) : AuthSignInResult
}
