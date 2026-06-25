package com.example.dynamicui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dynamicui.domain.model.UiComponent
import com.example.dynamicui.ui.components.NumberInputComponent
import com.example.dynamicui.ui.components.SliderComponent
import com.example.dynamicui.ui.components.TextFieldComponent
import com.example.dynamicui.ui.components.UnknownComponent

/**
 * Main engine traversing the list of business models to render Jetpack Compose dynamic UI.
 * Receives the current form state map and errors to bind values and validations reactively.
 */
@Composable
fun DynamicUiRenderer(
    components: List<UiComponent>,
    formValues: Map<String, Any>,
    validationErrors: Map<String, String>,
    onValueChange: (componentId: String, value: Any) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        components.forEach { component ->
            when (component) {
                is UiComponent.TextInput -> {
                    val currentValue = formValues[component.id] as? String ?: ""
                    val currentError = validationErrors[component.id]
                    TextFieldComponent(
                        model = component,
                        value = currentValue,
                        onValueChange = { onValueChange(component.id, it) },
                        errorMessage = currentError
                    )
                }
                is UiComponent.NumberInput -> {
                    val currentValue = formValues[component.id] as? String ?: ""
                    val currentError = validationErrors[component.id]
                    NumberInputComponent(
                        model = component,
                        value = currentValue,
                        onValueChange = { onValueChange(component.id, it) },
                        errorMessage = currentError
                    )
                }
                is UiComponent.SliderInput -> {
                    val currentValue = formValues[component.id] as? Float ?: component.min
                    SliderComponent(
                        model = component,
                        value = currentValue,
                        onValueChange = { onValueChange(component.id, it) }
                    )
                }
                is UiComponent.Unsupported -> UnknownComponent(
                    model = component
                )
            }
        }
    }
}
