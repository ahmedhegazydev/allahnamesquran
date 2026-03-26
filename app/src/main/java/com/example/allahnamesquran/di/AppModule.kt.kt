package com.example.allahnamesquran.di

import com.example.allahnamesquran.features.details.DetailsViewModel
import com.example.allahnamesquran.features.home.HomeViewModel
import com.example.allahnamesquran.features.onboarding.OnboardingViewModel
import com.example.allahnamesquran.features.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SplashViewModel() }
    viewModel { OnboardingViewModel() }
    viewModel { HomeViewModel() }
    viewModel { DetailsViewModel() }
}