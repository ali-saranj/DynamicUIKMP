package com.example.dynamicui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent
import kotlin.math.roundToInt

/**
 * Renders a slider selector with value range and increments based on [UiComponent.SliderInput].
 */
@Composable
fun SliderComponent(
    model: UiComponent.SliderInput,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderValue by remember { mutableStateOf(model.min) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = model.label)
            Spacer(modifier = Modifier.weight(1.0f))
            // Format to show cleanly
            Text(text = String.format("%.1f", sliderValue))
        }
        Slider(
            value = sliderValue,
            onValueChange = {
                val steppedValue = if (model.step > 0) {
                    (it / model.step).roundToInt() * model.step
                } else {
                    it
                }
                sliderValue = steppedValue.coerceIn(model.min, model.max)
                onValueChange(sliderValue)
            },
            valueRange = model.min..model.max,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
private fun String.Companion.format(format: String, vararg args: Any?): String {
    // Custom cross-platform formatting fallback
    val value = args[0] as Float
    val multiplier = 10 // for 1 decimal digit
    val rounded = (value * multiplier).roundToInt().toFloat() / multiplier
    return rounded.toString()
}
