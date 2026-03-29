package com.example.allahnamesquran

import androidx.room.Room
import com.example.allahnamesquran.data.auth.AuthRepository
import com.example.allahnamesquran.data.auth.SupabaseAuthManager
import com.example.allahnamesquran.data.local.AppDatabase
import com.example.allahnamesquran.data.preferences.AppPreferences
import com.example.allahnamesquran.data.remote.NetworkModule
import com.example.allahnamesquran.data.remote.supabase.SupabaseProvider
import com.example.allahnamesquran.data.repository.QuranRepository
import com.example.allahnamesquran.data.repository.QuranRepositoryImpl
import com.example.allahnamesquran.features.details.DetailsViewModel
import com.example.allahnamesquran.features.home.HomeViewModel
import com.example.allahnamesquran.features.onboarding.OnboardingViewModel
import com.example.allahnamesquran.features.signin.SignInViewModel
import com.example.allahnamesquran.features.splash.SplashViewModel
import com.example.allahnamesquran.notifications.AndroidDailyNameReminderScheduler
import com.example.allahnamesquran.notifications.DailyNameReminderScheduler
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
