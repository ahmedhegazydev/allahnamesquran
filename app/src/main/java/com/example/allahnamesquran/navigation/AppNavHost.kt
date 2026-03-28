package com.example.allahnamesquran.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.allahnamesquran.core.ui.theme.AllahNamesQuranTheme
import com.example.allahnamesquran.features.details.DetailsScreen
import com.example.allahnamesquran.features.home.HomeScreen
import com.example.allahnamesquran.features.onboarding.OnboardingScreen
import com.example.allahnamesquran.features.splash.SplashScreen
import com.example.allahnamesquran.features.splash.SplashViewModel
import com.example.allahnamesquran.data.repository.QuranRepository
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    // تم نقل navController للداخل لضمان استقرار (Stability) الـ AppNavHost
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
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.Onboarding.route) {
            OnboardingScreen(
                onStartClick = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.Home.route) {
            HomeScreen(
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
    KoinApplication(application = {
        modules(module {
            single<QuranRepository> {
                object : QuranRepository {
                    override suspend fun syncQuranIfNeeded() {}
                    override suspend fun isOnboardingSeen(): Boolean = false
                    override suspend fun setOnboardingSeen() {}
                }
            }
            factory { SplashViewModel(get()) }
        })
    }) {
        AllahNamesQuranTheme {
            AppNavHost()
        }
    }
}
