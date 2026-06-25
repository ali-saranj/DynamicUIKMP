package com.example.dynamicui.data.remote

import com.example.dynamicui.data.model.ResponseDto

/**
 * Service contract defining the REST API connection to fetch UI component configurations.
 */
interface DynamicUiApi {
    suspend fun fetchComponentResponse(source: MockSource = MockSource.LIVE): ResponseDto
}
