package app.asmaquran.mobile.features.onboarding

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import app.asmaquran.mobile.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: QuranRepository
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<Unit>()
    val navigationEvents: SharedFlow<Unit> = _navigationEvents

    fun onStartClicked() {
        viewModelScope.launch {
            repository.setOnboardingSeen()
            _navigationEvents.emit(Unit)
        }
    }
}
