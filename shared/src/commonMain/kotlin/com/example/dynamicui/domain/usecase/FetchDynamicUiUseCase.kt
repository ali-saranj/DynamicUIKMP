package com.example.dynamicui.domain.usecase

import com.example.dynamicui.data.remote.MockSource
import com.example.dynamicui.domain.model.UiComponent
import com.example.dynamicui.domain.repository.DynamicUiRepository

/**
 * Use case to fetch components and execute domain validation rules.
 */
class FetchDynamicUiUseCase(
    private val repository: DynamicUiRepository
) {
    suspend operator fun invoke(source: MockSource = MockSource.LIVE): List<UiComponent> {
        return repository.getComponentConfiguration(source)
    }
}
