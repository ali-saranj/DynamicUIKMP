package com.example.dynamicui.data.model

import kotlinx.serialization.Serializable

/**
 * Top level REST response containing screen configurations and exactly ONE dynamic component DTO
 * as specified in the interview scenario constraints.
 */
@Serializable
data class ResponseDto(
    val screenId: String,
    val title: String,
    val component: ComponentDto
)
