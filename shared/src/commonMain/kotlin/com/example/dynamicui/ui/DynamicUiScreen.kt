package com.example.dynamicui.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.presentation.DynamicUiIntent
import com.example.dynamicui.presentation.DynamicUiState
import com.example.dynamicui.presentation.DynamicUiViewModel

/**
 * Screen level Composable mapping UI state flow states into visible screens.
 * Contains forms fields, buttons, progress updates, and error state panels.
 */
@Composable
fun DynamicUiScreen(
    viewModel: DynamicUiViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val formValues = remember { mutableStateMapOf<String, Any>() }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val currentState = state) {
                is DynamicUiState.Idle,
                is DynamicUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DynamicUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.handleIntent(DynamicUiIntent.Refresh) }) {
                            Text("Retry")
                        }
                    }
                }
                is DynamicUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        DynamicUiRenderer(
                            components = currentState.components,
                            onValueChange = { componentId, value ->
                                formValues[componentId] = value
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.handleIntent(DynamicUiIntent.SubmitForm(formValues.toMap()))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit Form")
                        }
                    }
                }
            }
        }
    }
}
