package com.example.dynamicui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent

/**
 * Renders a dynamic text input layout based on [UiComponent.TextInput].
 */
@Composable
fun TextFieldComponent(
    model: UiComponent.TextInput,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = if (model.isRequired) "${model.label} *" else model.label)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            placeholder = { Text(text = model.placeholder) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
