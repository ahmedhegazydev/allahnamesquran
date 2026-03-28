package com.example.allahnamesquran.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.allahnamesquran.core.ui.components.AsmaTabRow
import com.example.allahnamesquran.core.ui.theme.QuranFontFamily
import com.example.allahnamesquran.features.home.HomeTab

@Composable
fun HomeHeader(
    searchQuery: String,
    selectedTab: HomeTab,
    allCount: Int,
    favoriteCount: Int,
    onSearchChanged: (String) -> Unit,
    onTabSelected: (HomeTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_title),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = QuranFontFamily
        )

        Text(
            text = stringResource(R.string.home_subtitle),
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            fontFamily = QuranFontFamily
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White.copy(alpha = 0.18f),
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = if (selectedTab == HomeTab.FAVORITES) {
                    stringResource(R.string.favorite_names_count, favoriteCount)
                } else {
                    stringResource(R.string.all_names_count, allCount)
                },
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily
            )
        }

        NameSearchBar(
            query = searchQuery,
            onQueryChange = onSearchChanged
        )

        AsmaTabRow(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
    }
}