package com.example.allahnamesquran.features.splash

import androidx.compose.runtime.Immutable

@Immutable
data class SplashUiState(
    val isLoading: Boolean = true,
    val destination: SplashDestination? = null
)

enum class SplashDestination {
    ONBOARDING,
    SIGN_IN,
    HOME
}

sealed interface SplashIntent {
    data object Start : SplashIntent
}
