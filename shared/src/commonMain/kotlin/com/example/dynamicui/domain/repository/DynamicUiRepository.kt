package com.example.dynamicui.domain.repository

import com.example.dynamicui.data.remote.MockSource
import com.example.dynamicui.domain.model.UiComponent

/**
 * Domain boundary contract representing the access to dynamic component configurations.
 * Satisfies Dependency Inversion - UI and presentation interact with this interface.
 */
interface DynamicUiRepository {
    suspend fun getComponentConfiguration(source: MockSource = MockSource.LIVE): List<UiComponent>
}
