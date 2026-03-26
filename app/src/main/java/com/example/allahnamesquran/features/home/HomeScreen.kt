package com.example.allahnamesquran.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.MaterialTheme
import com.example.allahnamesquran.features.home.components.EmptyNamesState
import com.example.allahnamesquran.features.home.components.HomeHeader
import com.example.allahnamesquran.features.home.components.NameCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
        onNameClick: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        HomeHeader(
            searchQuery = state.searchQuery,
            selectedTab = state.selectedTab,
            allCount = state.names.size,
            favoriteCount = state.names.count { it.isFavorite },
            onSearchChanged = { viewModel.onIntent(HomeIntent.SearchChanged(it)) },
            onTabSelected = { viewModel.onIntent(HomeIntent.TabSelected(it)) }
        )

        if (state.isEmpty) {
            EmptyNamesState(modifier = Modifier.weight(1f))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.visibleNames,
                    key = { it.id }
                ) { item ->
                    NameCard(
                        item = item,
                        onClick = { onNameClick(item.id) },
                        onFavoriteClick = {
                            viewModel.onIntent(HomeIntent.FavoriteClicked(item.id))
                        }
                    )
                }
            }
        }
    }
}