package app.asmaquran.mobile.features.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.theme.PrimaryGreen
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily

@Composable
fun SettingsHeader(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PrimaryGreen,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 26.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(52.dp)
                .background(
                    color = Color.White.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Color.White
            )
        }

        Text(
            text = stringResource(R.string.settings_title),
            color = Color.White,
            fontFamily = QuranFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 34.dp),
            textAlign = TextAlign.End
        )
    }
}
