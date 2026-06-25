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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent

/**
 * Renders a numeric text input with range validations based on [UiComponent.NumberInput].
 */
@Composable
fun NumberInputComponent(
    model: UiComponent.NumberInput,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "${model.label} (Range: ${model.min} - ${model.max})")
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                textValue = input
                val parsed = input.toIntOrNull()
                if (parsed == null && input.isNotEmpty()) {
                    errorMessage = "Please enter a valid integer"
                } else if (parsed != null && (parsed < model.min || parsed > model.max)) {
                    errorMessage = "Must be between ${model.min} and ${model.max}"
                } else {
                    errorMessage = null
                    onValueChange(parsed ?: 0)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(text = it) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
