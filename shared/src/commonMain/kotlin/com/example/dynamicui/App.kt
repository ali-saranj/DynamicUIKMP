package com.example.dynamicui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.dynamicui.di.appModule
import com.example.dynamicui.presentation.DynamicUiViewModel
import com.example.dynamicui.ui.DynamicUiScreen
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        MaterialTheme {
            val viewModel = koinViewModel<DynamicUiViewModel>()
            DynamicUiScreen(viewModel = viewModel)
        }
    }
}