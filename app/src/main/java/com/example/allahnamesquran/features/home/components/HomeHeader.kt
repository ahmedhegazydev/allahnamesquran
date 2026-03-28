package com.example.allahnamesquran.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahnamesquran.R
import com.example.allahnamesquran.core.ui.preview.PreviewSurface
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.home.HomeTab

@Composable
fun HomeHeader(
    searchQuery: String,
    selectedTab: HomeTab,
    allCount: Int,
    favoriteCount: Int,
    onSearchChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Text(
                text = stringResource(R.string.home_title),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.home_subtitle),
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 18.sp,
                fontFamily = QuranFontFamily,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White.copy(alpha = 0.12f),
                    RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (selectedTab == HomeTab.FAVORITES) {
                    Icons.Rounded.Favorite
                } else {
                    Icons.AutoMirrored.Rounded.MenuBook
                },
                contentDescription = null,
                tint = Color.White
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))

            Text(
                text = if (selectedTab == HomeTab.FAVORITES) {
                    stringResource(R.string.favorite_names_count, favoriteCount)
                } else {
                    stringResource(R.string.all_names_count, allCount)
                },
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily
            )
        }

        NameSearchBar(
            query = searchQuery,
            onQueryChange = onSearchChanged
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun HomeHeaderPreview() {
    PreviewSurface {
        HomeHeader(
            searchQuery = "الل",
            selectedTab = HomeTab.ALL,
            allCount = 100,
            favoriteCount = 12,
            onSearchChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun HomeHeaderFavoritesPreview() {
    PreviewSurface {
        HomeHeader(
            searchQuery = "رح",
            selectedTab = HomeTab.FAVORITES,
            allCount = 100,
            favoriteCount = 12,
            onSearchChanged = {}
        )
    }
}