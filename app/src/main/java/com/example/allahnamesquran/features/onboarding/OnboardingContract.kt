package com.example.allahnamesquran.features.onboarding

import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingUiState(
    val title: String = "مرحبًا بك",
    val subtitle: String = "اختر اسمًا من أسماء الله الحسنى لتستكشف الآيات التي ورد فيها في القرآن الكريم"
)

sealed interface OnboardingIntent {
    data object StartClicked : OnboardingIntent
}