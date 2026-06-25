package com.example.dynamicui.di

import com.example.dynamicui.data.remote.DynamicUiApi
import com.example.dynamicui.data.remote.KtorDynamicUiApi
import com.example.dynamicui.data.repository.DynamicUiRepositoryImpl
import com.example.dynamicui.domain.repository.DynamicUiRepository
import com.example.dynamicui.domain.usecase.FetchDynamicUiUseCase
import com.example.dynamicui.presentation.DynamicUiViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val appModule = module {
    // Network & API definitions
    single { KtorDynamicUiApi.createDefaultHttpClient() }
    single<DynamicUiApi> { KtorDynamicUiApi(get()) }

    // Repository definitions
    single<DynamicUiRepository> { DynamicUiRepositoryImpl(get()) }

    // Use cases
    factory { FetchDynamicUiUseCase(get()) }

    // ViewModel
    viewModel { DynamicUiViewModel(get()) }
}

/**
 * Common entry point to initialize Koin DI on KMP start.
 */
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
