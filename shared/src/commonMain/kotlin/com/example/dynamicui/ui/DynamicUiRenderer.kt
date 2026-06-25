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
 */
@Composable
fun DynamicUiRenderer(
    components: List<UiComponent>,
    onValueChange: (componentId: String, value: Any) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        components.forEach { component ->
            when (component) {
                is UiComponent.TextInput -> TextFieldComponent(
                    model = component,
                    onValueChange = { onValueChange(component.id, it) }
                )
                is UiComponent.NumberInput -> NumberInputComponent(
                    model = component,
                    onValueChange = { onValueChange(component.id, it) }
                )
                is UiComponent.SliderInput -> SliderComponent(
                    model = component,
                    onValueChange = { onValueChange(component.id, it) }
                )
                is UiComponent.Unsupported -> UnknownComponent(
                    model = component
                )
            }
        }
    }
}
