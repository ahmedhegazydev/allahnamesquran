package app.asmaquran.mobile.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.PrimaryGreen
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val viewModel: SplashViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.Start)
    }

    LaunchedEffect(state.destination) {
        when (state.destination) {
            SplashDestination.ONBOARDING -> onNavigateToOnboarding()
            SplashDestination.SIGN_IN -> onNavigateToSignIn()
            SplashDestination.HOME -> onNavigateToHome()
            null -> Unit
        }
    }

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(106.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                PrimaryGreen,
                                PrimaryGreen.copy(alpha = 0.9f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily
            )
            Text(
                text = stringResource(R.string.app_name),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily
            )

            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2F6D58, heightDp = 640)
@Composable
private fun SplashScreenPreview() {
    PreviewSurface {
        SplashScreenContent()
    }
}
