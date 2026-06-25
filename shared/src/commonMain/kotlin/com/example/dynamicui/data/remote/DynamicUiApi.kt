package com.example.dynamicui.data.remote

import com.example.dynamicui.data.model.ComponentDto

/**
 * Service contract defining the REST API connection to fetch UI component configurations.
 */
interface DynamicUiApi {
    suspend fun fetchComponents(): List<ComponentDto>
}
