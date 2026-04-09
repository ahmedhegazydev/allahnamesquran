package app.asmaquran.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.asmaquran.mobile.data.auth.AuthUserProfile
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.core.ui.preview.AppScreenPreviews
import app.asmaquran.mobile.core.ui.theme.AllahNamesQuranTheme
import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.model.AyahSearchResult
import app.asmaquran.mobile.data.preferences.SettingsPreferencesStore
import app.asmaquran.mobile.features.details.DetailsScreen
import app.asmaquran.mobile.features.home.HomeScreen
import app.asmaquran.mobile.features.onboarding.OnboardingScreen
import app.asmaquran.mobile.features.onboarding.OnboardingViewModel
import app.asmaquran.mobile.features.settings.SettingsScreen
import app.asmaquran.mobile.features.signin.SignInScreen
import app.asmaquran.mobile.features.signin.SignInViewModel
import app.asmaquran.mobile.features.splash.SplashScreen
import app.asmaquran.mobile.features.splash.SplashViewModel
import app.asmaquran.mobile.notifications.DailyNameReminderScheduler
import app.asmaquran.mobile.data.repository.QuranRepository
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
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        app.asmaquran.mobile.features.onboarding.OnboardingNavigationEvent.SignIn -> {
                            navController.navigate(AppRoute.SignIn.route) {
                                popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                            }
                        }

                        app.asmaquran.mobile.features.onboarding.OnboardingNavigationEvent.Home -> {
                            navigateToHomeFromAuth(navController)
                        }
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
                },
                onSettingsClick = {
                    navController.navigate(AppRoute.Settings.route)
                }
            )
        }

        composable(AppRoute.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSignInClick = {
                    navController.navigate(AppRoute.SignIn.route)
                },
                onReplayOnboarding = {
                    navController.navigate(AppRoute.Onboarding.route) {
                        popUpTo(AppRoute.Settings.route) { inclusive = true }
                    }
                },
                onResetApp = {
                    navController.navigate(AppRoute.Onboarding.route) {
                        popUpTo(AppRoute.Home.route) { inclusive = true }
                    }
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

@AppScreenPreviews
@Composable
fun AppNavHostPreview() {
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(module {
            single<AuthRepository> {
                object : AuthRepository {
                    override suspend fun hasActiveSession(): Boolean = false
                    override suspend fun getCurrentUserProfile(): AuthUserProfile? = null
                    override suspend fun isAuthPromptCompleted(): Boolean = false
                    override suspend fun markAuthPromptCompleted() = Unit
                    override suspend fun signInWithGoogle(activity: android.app.Activity) =
                        app.asmaquran.mobile.data.auth.AuthSignInResult.NotConfigured
                    override suspend fun signInWithGithub() =
                        app.asmaquran.mobile.data.auth.AuthSignInResult.NotConfigured
                    override suspend fun signOut() = Unit
                }
            }
            single<QuranRepository> {
                object : QuranRepository {
                    override suspend fun syncQuranIfNeeded() {}
                    override suspend fun isOnboardingSeen(): Boolean = false
                    override suspend fun setOnboardingSeen() {}
                    override fun getAllAllahNames(): List<AllahName> = emptyList()
                    override fun getAllahNameById(id: Int): AllahName? = null
                    override fun observeFavoriteNameIds() =
                        kotlinx.coroutines.flow.flowOf(emptySet<Int>())
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
            single<SettingsPreferencesStore> {
                object : SettingsPreferencesStore {
                    override val dailyReminderEnabled = kotlinx.coroutines.flow.flowOf(true)
                    override val dailyReminderHour = kotlinx.coroutines.flow.flowOf(9)
                    override val dailyReminderMinute = kotlinx.coroutines.flow.flowOf(0)
                    override val selectedLanguageTag = kotlinx.coroutines.flow.flowOf("ar")
                    override val selectedAppearanceMode = kotlinx.coroutines.flow.flowOf("system")
                    override suspend fun setDailyReminderEnabled(value: Boolean) = Unit
                    override suspend fun setDailyReminderTime(hour: Int, minute: Int) = Unit
                    override suspend fun setSelectedLanguageTag(value: String) = Unit
                    override suspend fun setSelectedAppearanceMode(value: String) = Unit
                    override suspend fun clearFavoriteNameIds() = Unit
                    override suspend fun resetAll() = Unit
                }
            }
            single<DailyNameReminderScheduler> {
                object : DailyNameReminderScheduler {
                    override suspend fun syncDailyReminder() = Unit
                }
            }
            factory { SplashViewModel(get(), get(), get()) }
            factory { OnboardingViewModel(get(), get()) }
            factory { SignInViewModel(get()) }
            factory { app.asmaquran.mobile.features.settings.SettingsViewModel(get(), get(), get()) }
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
