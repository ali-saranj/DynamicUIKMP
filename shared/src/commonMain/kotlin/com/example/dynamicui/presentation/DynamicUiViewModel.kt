package com.example.dynamicui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        loadUiConfiguration()
    }

    /**
     * Dispatcher method to accept UI Intents from Compose.
     */
    fun handleIntent(intent: DynamicUiIntent) {
        when (intent) {
            is DynamicUiIntent.Refresh -> loadUiConfiguration()
            is DynamicUiIntent.SubmitForm -> submitFormValues(intent.values)
        }
    }

    private fun loadUiConfiguration() {
        viewModelScope.launch {
            _uiState.value = DynamicUiState.Loading
            try {
                val components = fetchDynamicUiUseCase()
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
