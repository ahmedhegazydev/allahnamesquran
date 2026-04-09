package app.asmaquran.mobile.features.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class SignInUiState(
    val loadingProvider: SignInProvider? = null,
    @StringRes val errorMessageRes: Int? = null
) {
    val isLoading: Boolean
        get() = loadingProvider != null
}

enum class SignInProvider {
    GOOGLE,
    GITHUB
}

sealed interface SignInNavigationEvent {
    data object Home : SignInNavigationEvent
}
