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

    override suspend fun fetchComponentResponse(source: MockSource): ResponseDto {
        return when (source) {
            MockSource.LIVE -> httpClient.get("http://192.168.1.4:9095/screen/home").body()
            MockSource.HOME_MOCK -> getMockHomeResponse()
            MockSource.TEXT_FIELD -> getMockTextFieldResponse()
            MockSource.NUMBER_INPUT -> getMockNumberInputResponse()
            MockSource.SLIDER -> getMockSliderResponse()
            MockSource.UNSUPPORTED -> getMockUnsupportedResponse()
            MockSource.ERROR -> throw RuntimeException("Mock Network connection timed out (api.example.com)")
        }
    }

    private fun getMockHomeResponse(): ResponseDto {
        val jsonStr = """
            {
              "screenId": "home",
              "title": "Welcome Home",
              "component": {
                "type": "text",
                "id": "welcome_message",
                "label": "Leave a guestbook entry",
                "placeholder": "Type your message here...",
                "isRequired": false
              }
            }
        """.trimIndent()
        return defaultJson.decodeFromString(jsonStr)
    }

    private fun getMockTextFieldResponse(): ResponseDto {
        val jsonStr = """
            {
              "screenId": "home",
              "title": "User Input",
              "component": {
                "type": "text",
                "id": "user_prompt",
                "label": "Ask something",
                "placeholder": "Type your question...",
                "isRequired": true
              }
            }
        """.trimIndent()
        return defaultJson.decodeFromString(jsonStr)
    }

    private fun getMockNumberInputResponse(): ResponseDto {
        val jsonStr = """
            {
              "screenId": "home",
              "title": "Age Input",
              "component": {
                "type": "number",
                "id": "age",
                "label": "Age Selection",
                "placeholder": "Enter age",
                "minVal": 18,
                "maxVal": 99
              }
            }
        """.trimIndent()
        return defaultJson.decodeFromString(jsonStr)
    }

    private fun getMockSliderResponse(): ResponseDto {
        val jsonStr = """
            {
              "screenId": "home",
              "title": "Volume Screen",
              "component": {
                "type": "slider",
                "id": "volume",
                "label": "Volume Settings",
                "min": 0.0,
                "max": 100.0,
                "value": 50.0,
                "step": 5.0
              }
            }
        """.trimIndent()
        return defaultJson.decodeFromString(jsonStr)
    }

    private fun getMockUnsupportedResponse(): ResponseDto {
        val jsonStr = """
            {
              "screenId": "home",
              "title": "Unsupported Component",
              "component": {
                "type": "unsupported_checkbox",
                "id": "agreement",
                "label": "Accept Terms"
              }
            }
        """.trimIndent()
        return defaultJson.decodeFromString(jsonStr)
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
