package app.asmaquran.mobile.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.asmaquran.mobile.features.settings.SettingsAppearanceOption

private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    secondary = GoldAccent,
    background = AppBackground,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val DarkColors = lightColorScheme(
    primary = Color(0xFF1C7A61),
    secondary = Color(0xFFE2C77F),
    background = Color(0xFF0E1715),
    surface = Color(0xFF17211F),
    onPrimary = Color.White,
    onBackground = Color(0xFFF5F2EB),
    onSurface = Color(0xFFF5F2EB)
)

@Composable
fun AllahNamesQuranTheme(
    appearanceOption: SettingsAppearanceOption = SettingsAppearanceOption.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (appearanceOption) {
        SettingsAppearanceOption.LIGHT -> false
        SettingsAppearanceOption.DARK -> true
        SettingsAppearanceOption.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
