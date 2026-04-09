package app.asmaquran.mobile.features.onboarding

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: QuranRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<OnboardingNavigationEvent>()
    val navigationEvents: SharedFlow<OnboardingNavigationEvent> = _navigationEvents

    fun onStartClicked() {
        viewModelScope.launch {
            repository.setOnboardingSeen()
            val destination = if (
                authRepository.hasActiveSession() || authRepository.isAuthPromptCompleted()
            ) {
                OnboardingNavigationEvent.Home
            } else {
                OnboardingNavigationEvent.SignIn
            }
            _navigationEvents.emit(destination)
        }
    }
}

sealed interface OnboardingNavigationEvent {
    data object SignIn : OnboardingNavigationEvent
    data object Home : OnboardingNavigationEvent
}
