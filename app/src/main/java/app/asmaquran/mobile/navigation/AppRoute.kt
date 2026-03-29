
package app.asmaquran.mobile.navigation

sealed class AppRoute(val route: String) {
    data object Splash : AppRoute("splash")
    data object Onboarding : AppRoute("onboarding")
    data object SignIn : AppRoute("sign_in")
    data object Home : AppRoute("home")
    data object Details : AppRoute("details/{nameId}") {
        fun create(nameId: Int): String = "details/$nameId"
    }
}
