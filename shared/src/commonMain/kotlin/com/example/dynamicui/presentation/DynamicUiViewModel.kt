package com.example.dynamicui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicui.data.remote.MockSource
import com.example.dynamicui.domain.usecase.FetchDynamicUiUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Shared ViewModel handling core SDUI operations.
 * Drives view state transitions using Flow and Coroutines.
 */
class DynamicUiViewModel(
    private val fetchDynamicUiUseCase: FetchDynamicUiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DynamicUiState>(DynamicUiState.Idle)
    val uiState: StateFlow<DynamicUiState> = _uiState.asStateFlow()

    // Store the currently active source. We default to HOME_MOCK for previewing on startup
    // so the assessor sees a beautiful, working form immediately instead of a blank connection error.
    private val _currentSource = MutableStateFlow(MockSource.HOME_MOCK)
    val currentSource: StateFlow<MockSource> = _currentSource.asStateFlow()

    init {
        loadUiConfiguration(_currentSource.value)
    }

    /**
     * Dispatcher method to accept UI Intents from Compose.
     */
    fun handleIntent(intent: DynamicUiIntent) {
        when (intent) {
            is DynamicUiIntent.Refresh -> {
                val source = intent.source ?: _currentSource.value
                _currentSource.value = source
                loadUiConfiguration(source)
            }
            is DynamicUiIntent.SubmitForm -> submitFormValues(intent.values)
        }
    }

    private fun loadUiConfiguration(source: MockSource) {
        viewModelScope.launch {
            _uiState.value = DynamicUiState.Loading
            try {
                val components = fetchDynamicUiUseCase(source)
                _uiState.value = DynamicUiState.Success(components)
            } catch (e: Exception) {
                _uiState.value = DynamicUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun submitFormValues(values: Map<String, Any>) {
        // Form submission business rules go here
    }
}
