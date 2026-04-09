package app.asmaquran.mobile.data.auth

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import app.asmaquran.mobile.BuildConfig
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.data.remote.supabase.SupabaseProvider
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Github
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import java.util.UUID
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

class SupabaseAuthManager(
    private val supabaseProvider: SupabaseProvider,
    private val appPreferences: AppPreferences
) : AuthRepository {

    override suspend fun hasActiveSession(): Boolean {
        return supabaseProvider.client?.auth?.currentSessionOrNull() != null
    }

    override suspend fun getCurrentUserProfile(): AuthUserProfile? {
        val user = supabaseProvider.client?.auth?.currentUserOrNull() ?: return null
        val metadata = user.userMetadata
        val displayName = metadata?.get("full_name")?.jsonPrimitive?.contentOrNull
            ?: metadata?.get("name")?.jsonPrimitive?.contentOrNull
            ?: user.email?.substringBefore('@')

        return AuthUserProfile(
            displayName = displayName,
            email = user.email
        )
    }

    override suspend fun isAuthPromptCompleted(): Boolean {
        return appPreferences.isAuthPromptCompleted()
    }

    override suspend fun markAuthPromptCompleted() {
        appPreferences.setAuthPromptCompleted(true)
    }

    override suspend fun signInWithGoogle(activity: Activity): AuthSignInResult {
        val client = supabaseProvider.client ?: return AuthSignInResult.NotConfigured
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isBlank()) return AuthSignInResult.NotConfigured

        return try {
            val nonce = UUID.randomUUID().toString()
            val credentialManager = CredentialManager.create(activity)
            val option = GetSignInWithGoogleOption.Builder(
                BuildConfig.GOOGLE_WEB_CLIENT_ID
            ).setNonce(nonce).build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()

            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val credential = result.credential as? CustomCredential
                ?: return AuthSignInResult.NoToken

            if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                return AuthSignInResult.NoToken
            }

            val googleCredential = try {
                GoogleIdTokenCredential.createFrom(credential.data)
            } catch (_: GoogleIdTokenParsingException) {
                return AuthSignInResult.NoToken
            }

            client.auth.signInWith(IDToken) {
                provider = Google
                idToken = googleCredential.idToken
                this.nonce = nonce
            }

            markAuthPromptCompleted()
            AuthSignInResult.Success
        } catch (_: GetCredentialCancellationException) {
            AuthSignInResult.Cancelled
        } catch (throwable: Throwable) {
            AuthSignInResult.Failure(throwable)
        }
    }

    override suspend fun signInWithGithub(): AuthSignInResult {
        val client = supabaseProvider.client ?: return AuthSignInResult.NotConfigured

        return try {
            client.auth.signInWith(Github)
            AuthSignInResult.Started
        } catch (throwable: Throwable) {
            AuthSignInResult.Failure(throwable)
        }
    }

    override suspend fun signOut() {
        val client = supabaseProvider.client ?: return
        client.auth.signOut()
    }
}
