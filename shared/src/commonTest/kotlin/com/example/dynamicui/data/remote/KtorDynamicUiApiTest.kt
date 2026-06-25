package com.example.dynamicui.data.remote

import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.example.dynamicui.data.model.TextFieldDto
import com.example.dynamicui.data.model.NumberInputDto
import com.example.dynamicui.data.model.SliderDto
import com.example.dynamicui.data.model.UnknownComponentDto

/**
 * Unit tests validating that the mock JSON templates defined in [KtorDynamicUiApi]
 * are syntactically valid and parse correctly into their respective DTO representation.
 */
class KtorDynamicUiApiTest {

    @Test
    fun testMockHomeResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        val response = api.fetchComponentResponse(MockSource.HOME_MOCK)
        assertEquals("home", response.screenId)
        assertEquals("Welcome Home", response.title)
        val component = response.component
        assert(component is TextFieldDto)
        val textField = component as TextFieldDto
        assertEquals("welcome_message", textField.id)
        assertEquals("Leave a guestbook entry", textField.label)
        assertEquals(false, textField.isRequired)
    }

    @Test
    fun testMockTextFieldResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        val response = api.fetchComponentResponse(MockSource.TEXT_FIELD)
        assertEquals("home", response.screenId)
        assertEquals("User Input", response.title)
        val component = response.component
        assert(component is TextFieldDto)
        val textField = component as TextFieldDto
        assertEquals("user_prompt", textField.id)
        assertEquals("Ask something", textField.label)
        assertEquals(true, textField.isRequired)
    }

    @Test
    fun testMockNumberInputResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        val response = api.fetchComponentResponse(MockSource.NUMBER_INPUT)
        assertEquals("home", response.screenId)
        assertEquals("Age Input", response.title)
        val component = response.component
        assert(component is NumberInputDto)
        val numberInput = component as NumberInputDto
        assertEquals("age", numberInput.id)
        assertEquals(18, numberInput.minVal)
        assertEquals(99, numberInput.maxVal)
    }

    @Test
    fun testMockSliderResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        val response = api.fetchComponentResponse(MockSource.SLIDER)
        assertEquals("home", response.screenId)
        assertEquals("Volume Screen", response.title)
        val component = response.component
        assert(component is SliderDto)
        val slider = component as SliderDto
        assertEquals("volume", slider.id)
        assertEquals(0.0f, slider.min)
        assertEquals(100.0f, slider.max)
        assertEquals(5.0f, slider.step)
    }

    @Test
    fun testMockUnsupportedResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        val response = api.fetchComponentResponse(MockSource.UNSUPPORTED)
        assertEquals("home", response.screenId)
        val component = response.component
        assert(component is UnknownComponentDto)
        val unknown = component as UnknownComponentDto
        assertEquals("agreement", unknown.id)
        assertEquals("unsupported_checkbox", unknown.type)
    }

    @Test
    fun testMockErrorResponse() = runTest {
        val api = KtorDynamicUiApi(HttpClient())
        assertFailsWith<RuntimeException> {
            api.fetchComponentResponse(MockSource.ERROR)
        }
    }
}
