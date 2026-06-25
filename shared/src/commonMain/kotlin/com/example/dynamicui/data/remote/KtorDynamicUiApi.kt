package com.example.dynamicui.data.remote

import com.example.dynamicui.data.model.ResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Ktor implementation of the [DynamicUiApi].
 * Receives an [HttpClient] to perform remote REST API operations.
 */
class KtorDynamicUiApi(
    private val httpClient: HttpClient
) : DynamicUiApi {

    override suspend fun fetchComponentResponse(): ResponseDto {
        return httpClient.get("https://api.example.com/screen/home").body()
    }

    companion object {
        private val defaultJson by lazy {
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
            }
        }

        /**
         * Helper builder to construct an HTTP client with polymorphic serialization enabled.
         */
        fun createDefaultHttpClient(): HttpClient {
            return HttpClient {
                install(ContentNegotiation) {
                    json(defaultJson)
                }
            }
        }
    }
}
