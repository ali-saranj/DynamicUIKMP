package com.example.dynamicui.presentation

import com.example.dynamicui.data.remote.MockSource

/**
 * Intents representing user actions inside the Dynamic UI flow.
 */
sealed interface DynamicUiIntent {
    data class Refresh(val source: MockSource? = null) : DynamicUiIntent
    data class SubmitForm(val values: Map<String, Any>) : DynamicUiIntent
}
