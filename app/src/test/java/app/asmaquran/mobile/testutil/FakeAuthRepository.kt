package app.asmaquran.mobile.testutil

import android.app.Activity
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.auth.AuthSignInResult
import app.asmaquran.mobile.data.auth.AuthUserProfile

class FakeAuthRepository : AuthRepository {

    var hasActiveSession = false
    var authPromptCompleted = false
    var currentUserProfile: AuthUserProfile? = null
    var markAuthPromptCompletedCalls = 0
    var googleSignInResult: AuthSignInResult = AuthSignInResult.Success
    var githubSignInResult: AuthSignInResult = AuthSignInResult.Started
    var googleSignInCalls = 0
    var githubSignInCalls = 0
    var signOutCalls = 0
    var lastActivity: Activity? = null
    var profileAfterGoogleSignIn: AuthUserProfile? = null
    var profileAfterGithubSignIn: AuthUserProfile? = null

    override suspend fun hasActiveSession(): Boolean = hasActiveSession

    override suspend fun getCurrentUserProfile(): AuthUserProfile? = currentUserProfile

    override suspend fun isAuthPromptCompleted(): Boolean = authPromptCompleted

    override suspend fun markAuthPromptCompleted() {
        markAuthPromptCompletedCalls++
        authPromptCompleted = true
    }

    override suspend fun signInWithGoogle(activity: Activity): AuthSignInResult {
        googleSignInCalls++
        lastActivity = activity
        if (googleSignInResult == AuthSignInResult.Success) {
            hasActiveSession = true
            currentUserProfile = profileAfterGoogleSignIn ?: currentUserProfile
        }
        return googleSignInResult
    }

    override suspend fun signInWithGithub(): AuthSignInResult {
        githubSignInCalls++
        if (githubSignInResult == AuthSignInResult.Success) {
            hasActiveSession = true
            currentUserProfile = profileAfterGithubSignIn ?: currentUserProfile
        }
        return githubSignInResult
    }

    override suspend fun signOut() {
        signOutCalls++
        hasActiveSession = false
        currentUserProfile = null
        authPromptCompleted = false
    }
}
