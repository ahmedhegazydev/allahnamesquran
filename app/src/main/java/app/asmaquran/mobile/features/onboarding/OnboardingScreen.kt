package app.asmaquran.mobile.features.onboarding

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.AppScreenPreviews
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily

@Composable
fun OnboardingScreen(
    onStartClick: () -> Unit
) {
    OnboardingScreenContent(
        onStartClick = onStartClick,
        playSound = true
    )
}

@Composable
private fun OnboardingScreenContent(
    onStartClick: () -> Unit,
    playSound: Boolean
) {
    val context = LocalContext.current

    DisposableEffect(context, playSound) {
        val mediaPlayer = if (playSound) {
            MediaPlayer.create(context, R.raw.nrqhez49tgu)?.apply {
                isLooping = false
                start()
            }
        } else {
            null
        }

        onDispose {
            mediaPlayer?.run {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(top = 52.dp)
            ) {
                FeatureIconContainer(size = 96.dp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.onboarding_title),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = QuranFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.onboarding_description),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    lineHeight = 31.sp,
                    fontFamily = QuranFontFamily,
                    modifier = Modifier.fillMaxWidth(0.84f)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(22.dp),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    OnboardingFeatureItem(
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        title = stringResource(R.string.onboarding_feature_names_title),
                        subtitle = stringResource(R.string.onboarding_feature_names_subtitle)
                    )

                    OnboardingFeatureItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        title = stringResource(R.string.onboarding_feature_search_title),
                        subtitle = stringResource(R.string.onboarding_feature_search_subtitle)
                    )

//                    OnboardingFeatureItem(
//                        icon = {
//                            Icon(
//                                imageVector = Icons.Rounded.FavoriteBorder,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier.size(26.dp)
//                            )
//                        },
//                        title = stringResource(R.string.onboarding_feature_favorites_title),
//                        subtitle = stringResource(R.string.onboarding_feature_favorites_subtitle)
//                    )
                }
            }

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.onboarding_start),
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = QuranFontFamily
                )
            }
        }
    }
}

@Composable
private fun FeatureIconContainer(
    size: Dp = 56.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(size * 0.28f),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
        shadowElevation = 10.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@Composable
private fun OnboardingFeatureItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = QuranFontFamily,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                fontFamily = QuranFontFamily,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }

    }
}

@AppScreenPreviews
@Composable
private fun OnboardingScreenPreview() {
    PreviewSurface {
        OnboardingScreenContent(
            onStartClick = {},
            playSound = false
        )
    }
}
