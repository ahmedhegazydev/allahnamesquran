package com.example.allahnamesquran.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.allahnamesquran.R

val QuranFontFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

private fun TextStyle.withQuranFont() = copy(fontFamily = QuranFontFamily)

private val DefaultTypography = Typography()

val AppTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.withQuranFont(),
    displayMedium = DefaultTypography.displayMedium.withQuranFont(),
    displaySmall = DefaultTypography.displaySmall.withQuranFont(),
    headlineLarge = DefaultTypography.headlineLarge.withQuranFont(),
    headlineMedium = DefaultTypography.headlineMedium.withQuranFont(),
    headlineSmall = DefaultTypography.headlineSmall.withQuranFont(),
    titleLarge = DefaultTypography.titleLarge.withQuranFont(),
    titleMedium = DefaultTypography.titleMedium.withQuranFont(),
    titleSmall = DefaultTypography.titleSmall.withQuranFont(),
    bodyLarge = DefaultTypography.bodyLarge.withQuranFont(),
    bodyMedium = DefaultTypography.bodyMedium.withQuranFont(),
    bodySmall = DefaultTypography.bodySmall.withQuranFont(),
    labelLarge = DefaultTypography.labelLarge.withQuranFont(),
    labelMedium = DefaultTypography.labelMedium.withQuranFont(),
    labelSmall = DefaultTypography.labelSmall.withQuranFont()
)