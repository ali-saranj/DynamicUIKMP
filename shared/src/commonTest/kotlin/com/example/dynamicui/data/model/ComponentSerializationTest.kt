package com.example.dynamicui.data.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Unit tests validating polymorphic JSON deserialization of Dynamic UI component structures.
 */
class ComponentSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = true
    }

    @Test
    fun testDeserializeTextField() {
        val jsonStr = """
            {
                "type": "text",
                "id": "name_field",
                "label": "Full Name",
                "placeholder": "Enter your name",
                "isRequired": true
            }
        """.trimIndent()

        val component = json.decodeFromString<ComponentDto>(jsonStr)
        assertIs<TextFieldDto>(component)
        assertEquals("name_field", component.id)
        assertEquals("Full Name", component.label)
        assertEquals("Enter your name", component.placeholder)
        assertEquals(true, component.isRequired)
    }

    @Test
    fun testDeserializeNumberInput() {
        val jsonStr = """
            {
                "type": "number",
                "id": "age_field",
                "label": "Age",
                "minVal": 18,
                "maxVal": 99
            }
        """.trimIndent()

        val component = json.decodeFromString<ComponentDto>(jsonStr)
        assertIs<NumberInputDto>(component)
        assertEquals("age_field", component.id)
        assertEquals("Age", component.label)
        assertEquals(18, component.minVal)
        assertEquals(99, component.maxVal)
    }

    @Test
    fun testDeserializeSlider() {
        val jsonStr = """
            {
                "type": "slider",
                "id": "satisfaction",
                "label": "Satisfaction Rate",
                "min": 1.0,
                "max": 5.0,
                "value": 3.0,
                "step": 0.5
            }
        """.trimIndent()

        val component = json.decodeFromString<ComponentDto>(jsonStr)
        assertIs<SliderDto>(component)
        assertEquals("satisfaction", component.id)
        assertEquals(1.0f, component.min)
        assertEquals(5.0f, component.max)
        assertEquals(3.0f, component.value)
        assertEquals(0.5f, component.step)
    }

    @Test
    fun testDeserializeResponseWrapper() {
        val jsonStr = """
            {
                "screenId": "home",
                "title": "Volume Screen",
                "component": {
                    "type": "slider",
                    "id": "volume",
                    "label": "Volume",
                    "min": 0,
                    "max": 100,
                    "value": 50
                }
            }
        """.trimIndent()

        val response = json.decodeFromString<ResponseDto>(jsonStr)
        assertEquals("home", response.screenId)
        assertEquals("Volume Screen", response.title)
        
        val component = response.component
        assertIs<SliderDto>(component)
        assertEquals("volume", component.id)
        assertEquals(0f, component.min)
        assertEquals(100f, component.max)
        assertEquals(50f, component.value)
    }

    @Test
    fun testDeserializeUnknownFallback() {
        val jsonStr = """
            {
                "type": "unsupported_checkbox",
                "id": "agree_checkbox",
                "label": "I agree to terms",
                "some_random_key": "ignored"
            }
        """.trimIndent()

        val component = json.decodeFromString<ComponentDto>(jsonStr)
        assertIs<UnknownComponentDto>(component)
        assertEquals("agree_checkbox", component.id)
        assertEquals("unsupported_checkbox", component.type)
    }
}
