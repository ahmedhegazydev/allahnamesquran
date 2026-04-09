package app.asmaquran.mobile.data.remote.supabase

import app.asmaquran.mobile.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class SupabaseProvider {

    companion object {
        const val AUTH_CALLBACK_HOST = "login-callback"
    }

    val isConfigured: Boolean
        get() = BuildConfig.SUPABASE_URL.isNotBlank() &&
            BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank()

    val client: SupabaseClient? by lazy {
        if (!isConfigured) {
            null
        } else {
            createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY,
            ) {
                install(Auth) {
                    scheme = BuildConfig.APPLICATION_ID
                    host = AUTH_CALLBACK_HOST
                }
                install(Postgrest)
            }
        }
    }
}
