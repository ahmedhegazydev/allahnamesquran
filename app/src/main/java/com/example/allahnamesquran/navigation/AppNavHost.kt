package com.example.allahnamesquran.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.allahnamesquran.data.auth.AuthRepository
import com.example.allahnamesquran.core.ui.theme.AllahNamesQuranTheme
import com.example.allahnamesquran.data.model.AllahName
import com.example.allahnamesquran.data.model.AyahSearchResult
import com.example.allahnamesquran.features.details.DetailsScreen
import com.example.allahnamesquran.features.home.HomeScreen
import com.example.allahnamesquran.features.onboarding.OnboardingScreen
import com.example.allahnamesquran.features.onboarding.OnboardingViewModel
import com.example.allahnamesquran.features.signin.SignInScreen
import com.example.allahnamesquran.features.signin.SignInViewModel
import com.example.allahnamesquran.features.splash.SplashScreen
import com.example.allahnamesquran.features.splash.SplashViewModel
import com.example.allahnamesquran.notifications.DailyNameReminderScheduler
import com.example.allahnamesquran.data.repository.QuranRepository
import kotlinx.coroutines.flow.collect
import org.koin.compose.KoinApplication
import org.koin.androidx.compose.koinViewModel
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    dailyNameIdFromNotification: Int? = null
) {
    // NavHostController is unstable, so we keep it local to AppNavHost
    // This makes AppNavHost itself stable and skippable.
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Splash.route,
        modifier = modifier
    ) {
        composable(AppRoute.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(AppRoute.Onboarding.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate(AppRoute.SignIn.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.Onboarding.route) {
            val viewModel: OnboardingViewModel = koinViewModel()

            LaunchedEffect(viewModel) {
                viewModel.navigationEvents.collect {
                    navController.navigate(AppRoute.SignIn.route) {
                        popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                    }
                }
            }

            OnboardingScreen(
                onStartClick = {
                    viewModel.onStartClicked()
                }
            )
        }

        composable(AppRoute.SignIn.route) {
            SignInScreen(
                onNavigateToHome = {
                    navigateToHomeFromAuth(navController)
                }
            )
        }

        composable(AppRoute.Home.route) {
            HomeScreen(
                initialNameId = dailyNameIdFromNotification,
                onNameClick = { nameId ->
                    navController.navigate(AppRoute.Details.create(nameId))
                }
            )
        }

        composable(
            route = AppRoute.Details.route,
            arguments = listOf(
                navArgument("nameId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val nameId = backStackEntry.arguments?.getInt("nameId") ?: 0

            DetailsScreen(
                nameId = nameId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavHostPreview() {
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(module {
            single<AuthRepository> {
                object : AuthRepository {
                    override suspend fun hasActiveSession(): Boolean = false
                    override suspend fun isAuthPromptCompleted(): Boolean = false
                    override suspend fun markAuthPromptCompleted() = Unit
                    override suspend fun signInWithGoogle(activity: android.app.Activity) =
                        com.example.allahnamesquran.data.auth.AuthSignInResult.NotConfigured
                }
            }
            single<QuranRepository> {
                object : QuranRepository {
                    override suspend fun syncQuranIfNeeded() {}
                    override suspend fun isOnboardingSeen(): Boolean = false
                    override suspend fun setOnboardingSeen() {}
                    override fun getAllAllahNames(): List<AllahName> = emptyList()
                    override fun getAllahNameById(id: Int): AllahName? = null
                    override suspend fun getFavoriteNameIds(): Set<Int> {
                        return emptySet()
                    }

                    override suspend fun setFavoriteName(
                        id: Int,
                        isFavorite: Boolean
                    ) {
                    }

                    override suspend fun searchAyahsByAllahName(name: String): List<AyahSearchResult> =
                        emptyList()
                }
            }
            single<DailyNameReminderScheduler> {
                object : DailyNameReminderScheduler {
                    override fun scheduleDailyReminder() = Unit
                }
            }
            factory { SplashViewModel(get(), get(), get()) }
            factory { OnboardingViewModel(get()) }
            factory { SignInViewModel(get()) }
        })
    }), content = {
        AllahNamesQuranTheme {
            AppNavHost()
        }
    })
}

private fun navigateToHomeFromAuth(navController: NavHostController) {
    navController.navigate(AppRoute.Home.route) {
        popUpTo(AppRoute.SignIn.route) { inclusive = true }
    }
}
