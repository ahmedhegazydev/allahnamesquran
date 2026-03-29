package app.asmaquran.mobile

import androidx.room.Room
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.auth.SupabaseAuthManager
import app.asmaquran.mobile.data.local.AppDatabase
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.data.remote.NetworkModule
import app.asmaquran.mobile.data.remote.supabase.SupabaseProvider
import app.asmaquran.mobile.data.repository.QuranRepository
import app.asmaquran.mobile.data.repository.QuranRepositoryImpl
import app.asmaquran.mobile.features.details.DetailsViewModel
import app.asmaquran.mobile.features.home.HomeViewModel
import app.asmaquran.mobile.features.onboarding.OnboardingViewModel
import app.asmaquran.mobile.features.signin.SignInViewModel
import app.asmaquran.mobile.features.splash.SplashViewModel
import app.asmaquran.mobile.notifications.AndroidDailyNameReminderScheduler
import app.asmaquran.mobile.notifications.DailyNameReminderScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single { NetworkModule.provideQuranApiService() }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "allah_names_quran.db"
        ).build()
    }

    single { get<AppDatabase>().ayahDao() }

    single { AppPreferences(androidContext()) }
    single { SupabaseProvider() }
    single<AuthRepository> {
        SupabaseAuthManager(
            supabaseProvider = get(),
            appPreferences = get()
        )
    }

    single<DailyNameReminderScheduler> {
        AndroidDailyNameReminderScheduler(androidContext())
    }

    single<QuranRepository> {
        QuranRepositoryImpl(
            apiService = get(),
            ayahDao = get(),
            appPreferences = get()
        )
    }

    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailsViewModel)
}
