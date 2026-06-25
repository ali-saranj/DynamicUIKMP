package com.example.dynamicui.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Data Transfer Object representing dynamic component models fetched from network.
 * Managed polymorphically based on the "type" field using [ComponentDtoSerializer].
 */
@Serializable(with = ComponentDtoSerializer::class)
sealed interface ComponentDto {
    val id: String
    val label: String
}

@Serializable
@SerialName("text_input")
data class TextFieldDto(
    override val id: String,
    override val label: String,
    val placeholder: String? = null,
    val isRequired: Boolean = false
) : ComponentDto

@Serializable
@SerialName("number_input")
data class NumberInputDto(
    override val id: String,
    override val label: String,
    val minVal: Int? = null,
    val maxVal: Int? = null
) : ComponentDto

@Serializable
@SerialName("slider")
data class SliderDto(
    override val id: String,
    override val label: String,
    val minVal: Float = 0.0f,
    val maxVal: Float = 100.0f,
    val step: Float = 1.0f
) : ComponentDto

/**
 * DTO fallback representation to parse unsupported or newly-introduced API components.
 */
@Serializable
data class UnknownComponentDto(
    override val id: String = "unknown",
    override val label: String = "Unsupported Component Type",
    val type: String = "unknown"
) : ComponentDto

/**
 * Custom Polymorphic Serializer that handles deserialization mapping.
 * In case of unknown component types, it falls back to [UnknownComponentDto] instead of crashing.
 */
object ComponentDtoSerializer : JsonContentPolymorphicSerializer<ComponentDto>(ComponentDto::class) {
    override fun selectDeserializer(element: JsonElement): kotlinx.serialization.DeserializationStrategy<ComponentDto> {
        val jsonObject = element.jsonObject
        val type = jsonObject["type"]?.jsonPrimitive?.content
        return when (type) {
            "text", "text_input" -> TextFieldDto.serializer()
            "number", "number_input" -> NumberInputDto.serializer()
            "slider" -> SliderDto.serializer()
            else -> UnknownComponentDto.serializer()
        }
    }
}
