package com.example.dynamicui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent
import kotlin.math.roundToInt

/**
 * Renders a slider selector based on [UiComponent.SliderInput].
 * State is hoisted to prevent value resets when scrolled off-screen.
 */
@Composable
fun SliderComponent(
    model: UiComponent.SliderInput,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = model.label)
            Spacer(modifier = Modifier.weight(1.0f))
            Text(text = String.format("%.1f", value))
        }
        Slider(
            value = value,
            onValueChange = {
                val steppedValue = if (model.step > 0) {
                    (it / model.step).roundToInt() * model.step
                } else {
                    it
                }
                onValueChange(steppedValue.coerceIn(model.min, model.max))
            },
            valueRange = model.min..model.max,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun String.Companion.format(format: String, vararg args: Any?): String {
    val value = args[0] as Float
    val multiplier = 10
    val rounded = (value * multiplier).roundToInt().toFloat() / multiplier
    return rounded.toString()
}
