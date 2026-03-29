package com.example.allahnamesquran.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.core.ui.preview.PreviewHomeUiState
import com.example.allahnamesquran.core.ui.preview.PreviewSurface
import com.example.allahnamesquran.core.ui.theme.GoldAccent
import com.example.allahnamesquran.core.ui.theme.PrimaryGreen
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.home.DailyNameUiModel

@Composable
fun DailyNameCard(
    item: DailyNameUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.42f))
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF9EC),
                            Color.White,
                            Color(0xFFFFFCF4)
                        )
                    )
                )
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                PrimaryGreen.copy(alpha = 0.9f),
                                Color(0xFFEDE9D8),
                                Color.White
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(GoldAccent)
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "اسم اليوم",
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontFamily = QuranFontFamily,
                                fontSize = 18.sp
                            )
                            Text(
                                text = item.dateText,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = QuranFontFamily,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(PrimaryGreen)
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = QuranFontFamily,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.englishName,
                        color = PrimaryGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = item.shortDescription,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = QuranFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF7F4ED))
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = item.reflection,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = QuranFontFamily,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = GoldAccent.copy(alpha = 0.4f))

                    Text(
                        text = "آية اليوم",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontFamily = QuranFontFamily,
                        fontSize = 18.sp
                    )

                    Text(
                        text = item.ayahText,
                        color = PrimaryGreen,
                        fontFamily = QuranFontFamily,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )

                    Text(
                        text = item.ayahReference,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = QuranFontFamily,
                        fontSize = 15.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    Text(
                        text = "اضغط لمشاهدة جميع الآيات",
                        color = PrimaryGreen,
                        fontFamily = QuranFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = PrimaryGreen
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun DailyNameCardPreview() {
    PreviewSurface {
        DailyNameCard(
            item = PreviewHomeUiState.dailyName!!,
            onClick = {}
        )
    }
}
