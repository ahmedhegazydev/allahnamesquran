package app.asmaquran.mobile.features.signin

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.GoldAccent
import app.asmaquran.mobile.core.ui.theme.PrimaryGreen
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    onNavigateToHome: () -> Unit
) {
    val viewModel: SignInViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                SignInNavigationEvent.Home -> onNavigateToHome()
            }
        }
    }

    SignInScreenContent(
        state = state,
        onGoogleClick = {
            activity?.let(viewModel::signInWithGoogle)
        },
        onSkipClick = viewModel::skipForNow
    )
}

@Composable
private fun SignInScreenContent(
    state: SignInUiState,
    onGoogleClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFCF6),
                        MaterialTheme.colorScheme.background,
                        Color(0xFFFDF9F1)
                    )
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SignInHeroIcon()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sign_in_title),
                        color = PrimaryGreen,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = QuranFontFamily,
                        textAlign = TextAlign.Center
                    )

//                    Text(
//                        text = stringResource(R.string.sign_in_subtitle),
//                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.78f),
//                        fontSize = 20.sp,
//                        lineHeight = 31.sp,
//                        fontFamily = QuranFontFamily,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth(0.9f)
//                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    tonalElevation = 0.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SignInBenefitRow(text = stringResource(R.string.sign_in_benefit_sync))
                        SignInBenefitRow(text = stringResource(R.string.sign_in_benefit_access))
                        SignInBenefitRow(text = stringResource(R.string.sign_in_benefit_safe))
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onGoogleClick,
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp,
                            color = PrimaryGreen
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.sign_in_google_cta),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = QuranFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            GoogleMark(modifier = Modifier.size(22.dp))
                        }
                    }
                }

                OutlinedButton(
                    onClick = onSkipClick,
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.75f),
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in_skip),
                            fontFamily = QuranFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Icon(
                            imageVector = Icons.Rounded.Login,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                state.errorMessageRes?.let { messageRes ->
                    Text(
                        text = stringResource(messageRes),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        fontFamily = QuranFontFamily,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

//                Text(
//                    text = stringResource(R.string.sign_in_helper),
//                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.56f),
//                    fontFamily = QuranFontFamily,
//                    fontSize = 15.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
    }
}

@Composable
private fun SignInHeroIcon() {
    Box(contentAlignment = Alignment.TopEnd) {
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
                )
                ,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(46.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(GoldAccent)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Login,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SignInBenefitRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(GoldAccent.copy(alpha = 0.95f))
        )

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f),
            fontFamily = QuranFontFamily,
            fontSize = 18.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GoogleMark(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.18f
        val inset = stroke / 2
        val arcSize = Size(size.width - stroke, size.height - stroke)
        val topLeft = Offset(inset, inset)

        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 205f,
            sweepAngle = 78f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 128f,
            sweepAngle = 77f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 52f,
            sweepAngle = 76f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -28f,
            sweepAngle = 92f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(size.width * 0.54f, size.height * 0.51f),
            end = Offset(size.width * 0.92f, size.height * 0.51f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

private fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED, heightDp = 860)
@Composable
private fun SignInScreenPreview() {
    PreviewSurface {
        SignInScreenContent(
            state = SignInUiState(),
            onGoogleClick = {},
            onSkipClick = {}
        )
    }
}
