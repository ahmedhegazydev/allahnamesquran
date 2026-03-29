package com.example.allahnamesquran.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.allahnamesquran.core.ui.components.AsmaTabRow
import com.example.allahnamesquran.core.ui.preview.PreviewFavoriteHomeUiState
import com.example.allahnamesquran.core.ui.preview.PreviewHomeUiState
import com.example.allahnamesquran.core.ui.preview.PreviewSurface
import com.example.allahnamesquran.features.home.components.DailyNameCard
import com.example.allahnamesquran.features.home.components.EmptyNamesState
import com.example.allahnamesquran.features.home.components.HomeHeader
import com.example.allahnamesquran.features.home.components.NameCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    initialNameId: Int? = null,
    onNameClick: (Int) -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    var hasConsumedInitialName by rememberSaveable(initialNameId) { mutableStateOf(false) }
    var hasFocusedDailyCard by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadData)
    }

    LaunchedEffect(initialNameId, hasConsumedInitialName) {
        if (initialNameId != null && !hasConsumedInitialName) {
            hasConsumedInitialName = true
            onNameClick(initialNameId)
        }
    }

    LaunchedEffect(state.dailyName?.id, state.isLoading) {
        if (state.isLoading || state.dailyName == null || hasFocusedDailyCard) return@LaunchedEffect

        hasFocusedDailyCard = true
        gridState.animateScrollToItem(
            index = 1,
            scrollOffset = -24
        )
    }

    HomeScreenContent(
        state = state,
        gridState = gridState,
        onNameClick = onNameClick,
        onDailyNameClick = onNameClick,
        onSearchChanged = { viewModel.onIntent(HomeIntent.SearchChanged(it)) },
        onTabSelected = { viewModel.onIntent(HomeIntent.TabSelected(it)) },
        onFavoriteClick = { viewModel.onIntent(HomeIntent.FavoriteClicked(it)) }
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeUiState,
    gridState: LazyGridState,
    onNameClick: (Int) -> Unit,
    onDailyNameClick: (Int) -> Unit,
    onSearchChanged: (String) -> Unit,
    onTabSelected: (HomeTab) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        homeTopSection(
            state = state,
            onSearchChanged = onSearchChanged,
            onTabSelected = onTabSelected,
            onDailyNameClick = onDailyNameClick
        )

        if (state.isLoading) {
            items(6) {
                HomeNameCardSkeleton(shimmerAlpha = homeGridShimmerAlpha())
            }
        } else if (state.isEmpty) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyNamesState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else {
            items(
                items = state.visibleNames,
                key = { it.id }
            ) { item ->
                NameCard(
                    item = item,
                    onClick = { onNameClick(item.id) },
                    onFavoriteClick = { onFavoriteClick(item.id) }
                )
            }
        }
    }
}

@Composable
private fun homeGridShimmerAlpha(): Float {
    val shimmerAlpha by rememberInfiniteTransition(label = "home_grid_shimmer").animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "home_grid_shimmer_alpha"
    )

    return shimmerAlpha
}

private fun LazyGridScope.homeTopSection(
    state: HomeUiState,
    onSearchChanged: (String) -> Unit,
    onTabSelected: (HomeTab) -> Unit,
    onDailyNameClick: (Int) -> Unit
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        HomeHeader(
            searchQuery = state.searchQuery,
            selectedTab = state.selectedTab,
            allCount = if (state.isLoading) 0 else state.names.size,
            favoriteCount = state.names.count { it.isFavorite },
            onSearchChanged = onSearchChanged
        )
    }

    state.dailyName?.let { dailyName ->
        item(span = { GridItemSpan(maxLineSpan) }) {
            DailyNameCard(
                item = dailyName,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                onClick = { onDailyNameClick(dailyName.id) }
            )
        }
    }

    item(span = { GridItemSpan(maxLineSpan) }) {
        AsmaTabRow(
            selectedTab = state.selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun HomeNameCardSkeleton(shimmerAlpha: Float) {
    val placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(1.dp, Color(0xFFE9E3D8), RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .alpha(shimmerAlpha)
                .background(placeholderColor, RoundedCornerShape(100.dp))
                .fillMaxWidth(0.2f)
                .height(24.dp)
        )

        Box(
            modifier = Modifier
                .alpha(shimmerAlpha)
                .background(placeholderColor, RoundedCornerShape(12.dp))
                .fillMaxWidth(0.7f)
                .height(34.dp)
        )

        Spacer(
            modifier = Modifier
                .alpha(shimmerAlpha)
                .background(placeholderColor, RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(18.dp)
        )

        Spacer(
            modifier = Modifier
                .alpha(shimmerAlpha)
                .background(placeholderColor, RoundedCornerShape(12.dp))
                .fillMaxWidth(0.85f)
                .height(18.dp)
        )

        Spacer(
            modifier = Modifier
                .alpha(shimmerAlpha)
                .background(placeholderColor, RoundedCornerShape(12.dp))
                .fillMaxWidth(0.5f)
                .height(20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun HomeScreenPreview() {
    PreviewSurface {
        HomeScreenContent(
            state = PreviewHomeUiState,
            gridState = rememberLazyGridState(),
            onNameClick = {},
            onDailyNameClick = {},
            onSearchChanged = {},
            onTabSelected = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun HomeScreenFavoritesPreview() {
    PreviewSurface {
        HomeScreenContent(
            state = PreviewFavoriteHomeUiState,
            gridState = rememberLazyGridState(),
            onNameClick = {},
            onDailyNameClick = {},
            onSearchChanged = {},
            onTabSelected = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun HomeScreenLoadingPreview() {
    PreviewSurface {
        HomeScreenContent(
            state = PreviewHomeUiState.copy(isLoading = true, visibleNames = emptyList()),
            gridState = rememberLazyGridState(),
            onNameClick = {},
            onDailyNameClick = {},
            onSearchChanged = {},
            onTabSelected = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun HomeScreenEmptyPreview() {
    PreviewSurface {
        HomeScreenContent(
            state = PreviewHomeUiState.copy(visibleNames = emptyList(), isEmpty = true),
            gridState = rememberLazyGridState(),
            onNameClick = {},
            onDailyNameClick = {},
            onSearchChanged = {},
            onTabSelected = {},
            onFavoriteClick = {}
        )
    }
}
