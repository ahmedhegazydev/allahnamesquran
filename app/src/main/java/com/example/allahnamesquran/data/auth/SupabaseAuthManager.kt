package com.example.allahnamesquran.data.auth

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.allahnamesquran.BuildConfig
import com.example.allahnamesquran.data.preferences.AppPreferences
import com.example.allahnamesquran.data.remote.supabase.SupabaseProvider
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import java.util.UUID

class SupabaseAuthManager(
    private val supabaseProvider: SupabaseProvider,
    private val appPreferences: AppPreferences
) : AuthRepository {

    override suspend fun hasActiveSession(): Boolean {
        return supabaseProvider.client?.auth?.currentSessionOrNull() != null
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
}
