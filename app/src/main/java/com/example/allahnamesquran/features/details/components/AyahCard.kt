package com.example.allahnamesquran.features.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.R
import com.example.allahnamesquran.core.ui.theme.GoldAccent
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.details.AyahUiModel

@Composable
fun AyahCard(
    item: AyahUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Text(
                    text = item.ayahNumber.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = QuranFontFamily,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            Text(
                text = item.surahName,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuranFontFamily
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.ayah_reference, item.surahName, item.ayahNumber),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End
            )
        }

        Text(
            text = item.text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            lineHeight = 52.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = QuranFontFamily,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

    }
}