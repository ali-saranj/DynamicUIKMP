package com.example.dynamicui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.dynamicui.data.remote.KtorDynamicUiApi
import com.example.dynamicui.data.repository.DynamicUiRepositoryImpl
import com.example.dynamicui.domain.usecase.FetchDynamicUiUseCase
import com.example.dynamicui.presentation.DynamicUiViewModel
import com.example.dynamicui.ui.DynamicUiScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        // Instantiate Clean Architecture dependency graph
        val viewModel = remember {
            val httpClient = KtorDynamicUiApi.createDefaultHttpClient()
            val api = KtorDynamicUiApi(httpClient)
            val repository = DynamicUiRepositoryImpl(api)
            val useCase = FetchDynamicUiUseCase(repository)
            DynamicUiViewModel(useCase)
        }

        DynamicUiScreen(viewModel = viewModel)
    }
}