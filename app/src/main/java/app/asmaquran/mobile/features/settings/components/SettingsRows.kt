package app.asmaquran.mobile.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.theme.PrimaryGreen
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily

@Composable
fun AccountGuestRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(
            icon = Icons.Rounded.PersonOutline,
            tint = Color(0xFFADB5C3),
            backgroundColor = Color(0xFFF3F5F9)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(R.string.settings_guest_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            Text(
                text = stringResource(R.string.settings_guest_subtitle),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.58f),
                fontFamily = QuranFontFamily,
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun AccountSignedInRow(
    displayName: String,
    email: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(
            icon = Icons.Rounded.PersonOutline,
            tint = PrimaryGreen,
            backgroundColor = Color(0xFFEAF5F0)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = displayName,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            Text(
                text = email ?: stringResource(R.string.settings_signed_in_subtitle),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.58f),
                fontFamily = QuranFontFamily,
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ToggleSettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD9DEE7)
            )
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.58f),
                fontFamily = QuranFontFamily,
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ActionSettingRow(
    title: String,
    leadingIcon: ImageVector,
    subtitle: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val contentAlpha = if (enabled) 1f else 0.4f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .clickable(enabled = enabled && onClick != null) {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = Color(0xFF98A0B2).copy(alpha = contentAlpha),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = contentAlpha),
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            subtitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f * contentAlpha),
                    fontFamily = QuranFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            contentDescription = null,
            tint = Color(0xFFA7AFBF).copy(alpha = contentAlpha),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun RadioSettingRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryGreen,
                unselectedColor = Color(0xFFD0D6E0)
            )
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            subtitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.56f),
                    fontFamily = QuranFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun DangerSettingRow(
    title: String,
    leadingIcon: ImageVector,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = Color(0xFFFF4A4A),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                color = Color(0xFFFF3B3B),
                fontFamily = QuranFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
            subtitle?.let {
                Text(
                    text = it,
                    color = Color(0xFFFF6E6E),
                    fontFamily = QuranFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            contentDescription = null,
            tint = Color(0xFFFF6E6E),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun SettingsDivider(
    color: Color = Color(0xFFF0ECE4)
) {
    HorizontalDivider(
        thickness = 1.dp,
        color = color
    )
}

@Composable
private fun IconCircle(
    icon: ImageVector,
    tint: Color,
    backgroundColor: Color
) {
    Row(
        modifier = Modifier
            .size(54.dp)
            .background(backgroundColor, CircleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
    }
}
