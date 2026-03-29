package com.example.allahnamesquran.features.signin

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahnamesquran.R
import com.example.allahnamesquran.data.auth.AuthRepository
import com.example.allahnamesquran.data.auth.AuthSignInResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val state: StateFlow<SignInUiState> field = MutableStateFlow(SignInUiState())

    private val _navigationEvents = MutableSharedFlow<SignInNavigationEvent>()
    val navigationEvents: SharedFlow<SignInNavigationEvent> = _navigationEvents

    fun signInWithGoogle(activity: Activity) {
        if (state.value.isLoading) return

        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, errorMessageRes = null)

            when (authRepository.signInWithGoogle(activity)) {
                AuthSignInResult.Success -> {
                    state.value = SignInUiState()
                    _navigationEvents.emit(SignInNavigationEvent.Home)
                }

                AuthSignInResult.Cancelled -> {
                    state.value = SignInUiState(errorMessageRes = R.string.sign_in_error_cancelled)
                }

                AuthSignInResult.NotConfigured -> {
                    state.value = SignInUiState(errorMessageRes = R.string.sign_in_error_not_configured)
                }

                AuthSignInResult.NoToken -> {
                    state.value = SignInUiState(errorMessageRes = R.string.sign_in_error_google_token)
                }

                is AuthSignInResult.Failure -> {
                    state.value = SignInUiState(errorMessageRes = R.string.sign_in_error_generic)
                }
            }
        }
    }

    fun skipForNow() {
        if (state.value.isLoading) return

        viewModelScope.launch {
            authRepository.markAuthPromptCompleted()
            _navigationEvents.emit(SignInNavigationEvent.Home)
        }
    }
}
