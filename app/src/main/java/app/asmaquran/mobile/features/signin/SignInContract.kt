package app.asmaquran.mobile.features.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class SignInUiState(
    val isLoading: Boolean = false,
    @StringRes val errorMessageRes: Int? = null
)

sealed interface SignInNavigationEvent {
    data object Home : SignInNavigationEvent
}
