package com.example.dynamicui.data.repository

import com.example.dynamicui.data.model.ComponentDto
import com.example.dynamicui.data.model.TextFieldDto
import com.example.dynamicui.data.model.NumberInputDto
import com.example.dynamicui.data.model.SliderDto
import com.example.dynamicui.data.model.UnknownComponentDto
import com.example.dynamicui.data.remote.DynamicUiApi
import com.example.dynamicui.data.remote.MockSource
import com.example.dynamicui.domain.model.UiComponent
import com.example.dynamicui.domain.repository.DynamicUiRepository

/**
 * Concrete implementation of [DynamicUiRepository].
 * Fetches data from network and maps the DTO data classes into domain models.
 */
class DynamicUiRepositoryImpl(
    private val api: DynamicUiApi
) : DynamicUiRepository {

    override suspend fun getComponentConfiguration(source: MockSource): List<UiComponent> {
        val response = api.fetchComponentResponse(source)
        return listOf(response.component.toDomain())
    }

    private fun ComponentDto.toDomain(): UiComponent = when (this) {
        is TextFieldDto -> UiComponent.TextInput(
            id = id,
            label = label,
            placeholder = placeholder.orEmpty(),
            isRequired = isRequired
        )
        is NumberInputDto -> UiComponent.NumberInput(
            id = id,
            label = label,
            min = minVal ?: 0,
            max = maxVal ?: 100
        )
        is SliderDto -> UiComponent.SliderInput(
            id = id,
            label = label,
            min = min,
            max = max,
            defaultValue = value,
            step = step
        )
        is UnknownComponentDto -> UiComponent.Unsupported(
            id = id,
            typeName = type
        )
    }
}
