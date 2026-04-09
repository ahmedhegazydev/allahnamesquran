package app.asmaquran.mobile.features.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import app.asmaquran.mobile.data.auth.AuthProviderType

@Immutable
data class SignInUiState(
    val loadingProvider: AuthProviderType? = null,
    @StringRes val errorMessageRes: Int? = null
) {
    val isLoading: Boolean
        get() = loadingProvider != null
}

sealed interface SignInNavigationEvent {
    data object Home : SignInNavigationEvent
}
