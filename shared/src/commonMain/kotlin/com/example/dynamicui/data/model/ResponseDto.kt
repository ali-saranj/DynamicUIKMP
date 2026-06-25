package com.example.dynamicui.data.model

import kotlinx.serialization.Serializable

/**
 * Top level REST response containing the layout information and the list of dynamic component DTOs.
 */
@Serializable
data class ResponseDto(
    val title: String = "Dynamic Form",
    val components: List<ComponentDto> = emptyList()
)
