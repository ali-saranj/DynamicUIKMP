package com.example.dynamicui.presentation

import com.example.dynamicui.domain.model.UiComponent

/**
 * State representation for the Dynamic UI Screen.
 */
sealed interface DynamicUiState {
    data object Idle : DynamicUiState
    data object Loading : DynamicUiState
    data class Success(val components: List<UiComponent>) : DynamicUiState
    data class Error(val message: String) : DynamicUiState
}
