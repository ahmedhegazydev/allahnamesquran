package com.example.allahnamesquran.features.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.R
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.details.AyahUiModel

@Composable
fun AyahCard(
    item: AyahUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = item.text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = QuranFontFamily
        )

        Text(
            text = stringResource(R.string.ayah_reference, item.surahName, item.ayahNumber),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = QuranFontFamily
        )
    }
}