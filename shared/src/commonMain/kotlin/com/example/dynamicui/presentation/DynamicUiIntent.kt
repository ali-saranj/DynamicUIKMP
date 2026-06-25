package com.example.dynamicui.presentation

/**
 * Intents representing user actions inside the Dynamic UI flow.
 */
sealed interface DynamicUiIntent {
    data object Refresh : DynamicUiIntent
    data class SubmitForm(val values: Map<String, Any>) : DynamicUiIntent
}
