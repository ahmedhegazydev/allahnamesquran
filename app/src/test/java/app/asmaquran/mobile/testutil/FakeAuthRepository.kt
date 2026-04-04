package app.asmaquran.mobile.testutil

import android.app.Activity
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.auth.AuthSignInResult

class FakeAuthRepository : AuthRepository {

    var hasActiveSession = false
    var authPromptCompleted = false
    var markAuthPromptCompletedCalls = 0
    var signInResult: AuthSignInResult = AuthSignInResult.Success
    var signInCalls = 0
    var lastActivity: Activity? = null

    override suspend fun hasActiveSession(): Boolean = hasActiveSession

    override suspend fun isAuthPromptCompleted(): Boolean = authPromptCompleted

    override suspend fun markAuthPromptCompleted() {
        markAuthPromptCompletedCalls++
        authPromptCompleted = true
    }

    override suspend fun signInWithGoogle(activity: Activity): AuthSignInResult {
        signInCalls++
        lastActivity = activity
        return signInResult
    }
}
