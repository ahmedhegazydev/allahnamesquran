package com.example.allahnamesquran.features.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.allahnamesquran.R
import com.example.allahnamesquran.features.details.components.AyahCard
import com.example.allahnamesquran.features.details.components.DetailsHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    nameId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: DetailsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(nameId) {
        viewModel.onIntent(DetailsIntent.LoadData(nameId))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DetailsHeader(
                name = state.name,
                transliteration = state.transliteration,
                meaning = state.meaning,
                description = state.description,
                ayahsCount = state.ayahsCount,
                isFavorite = state.isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = {
                    viewModel.onIntent(DetailsIntent.ToggleFavorite)
                }
            )
        }

        item {
            Text(
                text = stringResource(R.string.ayahs_section_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        items(
            items = state.ayahs,
            key = { it.id }
        ) { ayah ->
            AyahCard(
                item = ayah
            )
        }
    }
}