package com.example.dynamicui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent

/**
 * Renders a dynamic text input layout based on [UiComponent.TextInput].
 * The state is hoisted to prevent value loss during scroll composition cycles.
 */
@Composable
fun TextFieldComponent(
    model: UiComponent.TextInput,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = if (model.isRequired) "${model.label} *" else model.label)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = model.placeholder) },
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(text = it) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
