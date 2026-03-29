package app.asmaquran.mobile.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily

@Composable
fun EmptyNamesState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
        )

        Text(
            text = stringResource(R.string.empty_results_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(R.string.empty_results_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 18.sp,
            fontFamily = QuranFontFamily,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun EmptyNamesStatePreview() {
    PreviewSurface {
        EmptyNamesState()
    }
}