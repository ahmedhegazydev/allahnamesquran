package app.asmaquran.mobile.features.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import app.asmaquran.mobile.core.ui.theme.SurfaceWhite
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.PreviewDetailsUiState
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily
import app.asmaquran.mobile.features.details.components.AyahCard
import app.asmaquran.mobile.features.details.components.DetailsHeader
import app.asmaquran.mobile.features.details.share.DetailsShareHelper
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

    DetailsScreenContent(
        state = state,
        onBackClick = onBackClick,
        onFavoriteClick = { viewModel.onIntent(DetailsIntent.ToggleFavorite) }
    )
}

@Composable
private fun DetailsScreenContent(
    state: DetailsUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            DetailsHeader(
                name = state.name,
                englishName = state.englishName,
                description = state.description,
                isFavorite = state.isFavorite,
                onBackClick = onBackClick,
                onShareClick = { DetailsShareHelper.shareText(context, state) },
                onFavoriteClick = onFavoriteClick
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.ayahs_section_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = QuranFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    color = SurfaceWhite.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    Text(
                        text = stringResource(R.string.ayahs_count, state.ayahsCount),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = QuranFontFamily,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
            }
        }

        items(
            items = state.ayahs,
            key = { it.id }
        ) { ayah ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AyahCard(item = ayah)
            }
        }

        item {
            Box(modifier = Modifier.padding(bottom = 12.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun DetailsScreenPreview() {
    PreviewSurface {
        DetailsScreenContent(
            state = PreviewDetailsUiState,
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}