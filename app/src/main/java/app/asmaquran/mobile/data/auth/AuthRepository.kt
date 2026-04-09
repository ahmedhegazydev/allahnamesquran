package app.asmaquran.mobile.data.auth

import android.app.Activity

interface AuthRepository {
    suspend fun hasActiveSession(): Boolean
    suspend fun getCurrentUserProfile(): AuthUserProfile?
    suspend fun isAuthPromptCompleted(): Boolean
    suspend fun markAuthPromptCompleted()
    suspend fun signInWithGoogle(activity: Activity): AuthSignInResult
    suspend fun signInWithGithub(): AuthSignInResult
    suspend fun signOut()
}

data class AuthUserProfile(
    val displayName: String?,
    val email: String?
)

sealed interface AuthSignInResult {
    data object Success : AuthSignInResult
    data object Started : AuthSignInResult
    data object Cancelled : AuthSignInResult
    data object NotConfigured : AuthSignInResult
    data object NoToken : AuthSignInResult
    data class Failure(val throwable: Throwable) : AuthSignInResult
}
