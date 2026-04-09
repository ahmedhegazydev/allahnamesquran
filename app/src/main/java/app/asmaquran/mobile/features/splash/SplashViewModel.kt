package app.asmaquran.mobile.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.repository.QuranRepository
import app.asmaquran.mobile.notifications.DailyNameReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: QuranRepository,
    private val authRepository: AuthRepository,
    private val dailyNameReminderScheduler: DailyNameReminderScheduler
) : ViewModel() {

    val state: StateFlow<SplashUiState> field = MutableStateFlow(SplashUiState())

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.Start -> start()
        }
    }

    private fun start() {
        if (state.value.destination != null) return

        viewModelScope.launch {
            try {
                repository.syncQuranIfNeeded()
                dailyNameReminderScheduler.syncDailyReminder()
                val onboardingSeen = repository.isOnboardingSeen()
                val hasActiveSession = authRepository.hasActiveSession()
                val authPromptCompleted = authRepository.isAuthPromptCompleted()

                state.value = SplashUiState(
                    isLoading = false,
                    destination = when {
                        !onboardingSeen -> SplashDestination.ONBOARDING
                        hasActiveSession || authPromptCompleted -> SplashDestination.HOME
                        else -> SplashDestination.SIGN_IN
                    }
                )
            } catch (e: Exception) {
                state.value = SplashUiState(
                    isLoading = false,
                    destination = SplashDestination.ONBOARDING
                )
            }
        }
    }
}
