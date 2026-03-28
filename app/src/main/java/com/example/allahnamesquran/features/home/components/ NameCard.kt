package com.example.allahnamesquran.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.R
import com.example.allahnamesquran.core.ui.theme.GoldAccent
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.home.NameUiModel

@Composable
fun NameCard(
    item: NameUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(1.dp, Color(0xFFE9E3D8), RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                contentAlignment = Alignment.TopStart
            ) {
                Icon(
                    imageVector = if (item.isFavorite) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = if (item.isFavorite) Color(0xFFE95454) else Color(0xFFB4B8C0),
                    modifier = Modifier.clickable(onClick = onFavoriteClick)
                )
            }

            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily,
                textAlign = TextAlign.End
            )
        }

        Text(
            text = item.description,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
            fontSize = 15.sp,
            lineHeight = 23.sp,
            fontFamily = QuranFontFamily,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "•",
                color = GoldAccent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(R.string.ayahs_count, item.ayahCount),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                fontSize = 16.sp,
                fontFamily = QuranFontFamily
            )
        }
    }
}