package com.example.allahnamesquran.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.allahnamesquran.features.home.components.EmptyNamesState
import com.example.allahnamesquran.features.home.components.HomeHeader
import com.example.allahnamesquran.features.home.components.NameCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNameClick: (Int) -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()
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
            allCount = if (state.isLoading) 0 else state.names.size,
            favoriteCount = state.names.count { it.isFavorite },
            onSearchChanged = { viewModel.onIntent(HomeIntent.SearchChanged(it)) },
            onTabSelected = { viewModel.onIntent(HomeIntent.TabSelected(it)) }
        )

        if (state.isLoading) {
            HomeNamesGridSkeleton()
        } else if (state.isEmpty) {
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

@Composable
private fun HomeNamesGridSkeleton() {
    val shimmerAlpha by rememberInfiniteTransition(label = "home_grid_shimmer").animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "home_grid_shimmer_alpha"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(6) {
            HomeNameCardSkeleton(shimmerAlpha = shimmerAlpha)
        }
    }
}

@Composable
private fun HomeNameCardSkeleton(shimmerAlpha: Float) {
    val placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
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