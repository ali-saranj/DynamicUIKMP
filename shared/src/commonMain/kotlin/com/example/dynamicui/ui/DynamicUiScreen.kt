package com.example.dynamicui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dynamicui.domain.model.UiComponent
import com.example.dynamicui.localization.Localizer
import com.example.dynamicui.presentation.DynamicUiIntent
import com.example.dynamicui.presentation.DynamicUiState
import com.example.dynamicui.presentation.DynamicUiViewModel

/**
 * Screen level Composable mapping UI state flows into screen layouts.
 * Features MVI state switching, validation, dynamic color rendering, i18n, and pull-to-refresh.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicUiScreen(
    viewModel: DynamicUiViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val formValues = remember { mutableStateMapOf<String, Any>() }
    val validationErrors = remember { mutableStateMapOf<String, String>() }

    val isRefreshing = state is DynamicUiState.Loading
    val localStrings = Localizer.strings

    // Validation engine helper
    fun validateForm(components: List<UiComponent>): Boolean {
        validationErrors.clear()
        var isValid = true
        components.forEach { component ->
            when (component) {
                is UiComponent.TextInput -> {
                    val value = formValues[component.id] as? String ?: ""
                    if (component.isRequired && value.isBlank()) {
                        validationErrors[component.id] = localStrings.fieldRequired
                        isValid = false
                    }
                }
                is UiComponent.NumberInput -> {
                    val value = formValues[component.id] as? String ?: ""
                    if (value.isBlank()) {
                        validationErrors[component.id] = localStrings.fieldRequired
                        isValid = false
                    } else {
                        val parsed = value.toIntOrNull()
                        if (parsed == null) {
                            validationErrors[component.id] = localStrings.invalidInteger
                            isValid = false
                        } else if (parsed < component.min || parsed > component.max) {
                            validationErrors[component.id] = localStrings.mustBeBetween
                                .replaceFirst("%d", component.min.toString())
                                .replaceFirst("%d", component.max.toString())
                            isValid = false
                        }
                    }
                }
                else -> {}
            }
        }
        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localStrings.dynamicForm, style = MaterialTheme.typography.titleMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val currentState = state) {
                is DynamicUiState.Idle -> {
                    // Idle state trigger handled in ViewModel
                }
                is DynamicUiState.Loading -> {
                    // Show progress indicator if not refreshing via swipe down
                    if (!isRefreshing) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
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
                        Button(
                            onClick = { viewModel.handleIntent(DynamicUiIntent.Refresh) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(localStrings.retry)
                        }
                    }
                }
                is DynamicUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { viewModel.handleIntent(DynamicUiIntent.Refresh) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = localStrings.infoMessage,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            DynamicUiRenderer(
                                components = currentState.components,
                                formValues = formValues,
                                validationErrors = validationErrors,
                                onValueChange = { componentId, value ->
                                    formValues[componentId] = value
                                    // Remove validation warning upon live user typing
                                    validationErrors.remove(componentId)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (validateForm(currentState.components)) {
                                        viewModel.handleIntent(DynamicUiIntent.SubmitForm(formValues.toMap()))
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text(localStrings.submitForm, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
