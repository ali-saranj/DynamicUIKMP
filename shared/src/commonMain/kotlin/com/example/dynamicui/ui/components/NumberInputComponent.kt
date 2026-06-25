package com.example.dynamicui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent

/**
 * Renders a numeric text input based on [UiComponent.NumberInput].
 * State is hoisted to preserve inputs during list scrolling.
 */
@Composable
fun NumberInputComponent(
    model: UiComponent.NumberInput,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "${model.label} (Range: ${model.min} - ${model.max})")
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                // Clean input validation: Allow only digits or empty string to prevent non-digit typing
                if (input.isEmpty() || input.all { it.isDigit() }) {
                    onValueChange(input)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(text = it) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
