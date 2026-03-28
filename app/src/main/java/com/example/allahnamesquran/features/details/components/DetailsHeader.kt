package com.example.allahnamesquran.features.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.core.ui.preview.PreviewSurface
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily

@Composable
fun DetailsHeader(
    name: String,
    englishName: String,
    description: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onForceCrashClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderActionButton(
                onClick = onBackClick,
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                onForceCrashClick?.let { forceCrashClick ->
                    HeaderActionButton(
                        onClick = forceCrashClick,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.BugReport,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    )
                }

                HeaderActionButton(
                    onClick = onShareClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                HeaderActionButton(
                    onClick = onFavoriteClick,
                    icon = {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily,
                textAlign = TextAlign.Center
            )

            Text(
                text = englishName,
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        if (description.isNotBlank()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(22.dp),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    lineHeight = 34.sp,
                    fontFamily = QuranFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun DetailsHeaderPreview() {
    PreviewSurface {
        DetailsHeader(
            name = "اللَّهُ",
            englishName = "ALLAH",
            description = "هو الاسم الدال على الذات الإلهية الجامعة لجميع صفات الكمال، المنفرد بالألوهية والعبادة.",
            isFavorite = true,
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
            onForceCrashClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun DetailsHeaderWithoutDescriptionPreview() {
    PreviewSurface {
        DetailsHeader(
            name = "الرَّحْمٰنُ",
            englishName = "AR-RAHMAN",
            description = "",
            isFavorite = false,
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
            onForceCrashClick = {}
        )
    }
}

@Composable
private fun HeaderActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.12f),
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.size(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
        }
    }
}
