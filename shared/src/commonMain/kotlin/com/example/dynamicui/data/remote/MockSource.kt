package com.example.dynamicui.data.remote

/**
 * Enumeration representing the target data source for the Dynamic UI.
 * Allows switching between the live server response and various offline mock components.
 */
enum class MockSource {
    LIVE,
    HOME_MOCK,
    TEXT_FIELD,
    NUMBER_INPUT,
    SLIDER,
    UNSUPPORTED,
    ERROR
}
