package com.example.allahnamesquran.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahnamesquran.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: QuranRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashUiState())
    val state: StateFlow<SplashUiState> = _state.asStateFlow()

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.Start -> start()
        }
    }

    private fun start() {
        if (_state.value.destination != null) return

        viewModelScope.launch {
            try {
                repository.syncQuranIfNeeded()
                val onboardingSeen = repository.isOnboardingSeen()

                _state.value = SplashUiState(
                    isLoading = false,
                    destination = if (onboardingSeen) {
                        SplashDestination.HOME
                    } else {
                        SplashDestination.ONBOARDING
                    }
                )
            } catch (e: Exception) {
                _state.value = SplashUiState(
                    isLoading = false,
                    destination = SplashDestination.ONBOARDING
                )
            }
        }
    }
}