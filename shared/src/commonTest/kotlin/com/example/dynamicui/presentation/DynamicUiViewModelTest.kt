package com.example.dynamicui.presentation

import com.example.dynamicui.domain.model.UiComponent
import com.example.dynamicui.domain.repository.DynamicUiRepository
import com.example.dynamicui.domain.usecase.FetchDynamicUiUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Unit tests validating state transitions inside [DynamicUiViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DynamicUiViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialLoadingAndSuccessState() = runTest {
        val mockRepository = FakeDynamicUiRepository(
            result = listOf(
                UiComponent.TextInput("id1", "Label 1", "Placeholder 1", false)
            )
        )
        val useCase = FetchDynamicUiUseCase(mockRepository)
        val viewModel = DynamicUiViewModel(useCase)

        val state = viewModel.uiState.value
        assertIs<DynamicUiState.Success>(state)
        assertEquals(1, state.components.size)
        assertEquals("id1", state.components[0].id)
    }

    @Test
    fun testLoadingErrorState() = runTest {
        val mockRepository = FakeDynamicUiRepository(shouldThrow = true)
        val useCase = FetchDynamicUiUseCase(mockRepository)
        val viewModel = DynamicUiViewModel(useCase)

        val state = viewModel.uiState.value
        assertIs<DynamicUiState.Error>(state)
        assertEquals("Failed to fetch config", state.message)
    }

    @Test
    fun testRefreshIntent() = runTest {
        val mockRepository = FakeDynamicUiRepository(
            result = listOf(
                UiComponent.TextInput("id1", "Label 1", "Placeholder 1", false)
            )
        )
        val useCase = FetchDynamicUiUseCase(mockRepository)
        val viewModel = DynamicUiViewModel(useCase)

        // Modify repository response before refresh
        mockRepository.result = listOf(
            UiComponent.TextInput("id1", "Updated Label", "Placeholder 1", false),
            UiComponent.NumberInput("id2", "Age", 0, 100)
        )

        viewModel.handleIntent(DynamicUiIntent.Refresh)

        val state = viewModel.uiState.value
        assertIs<DynamicUiState.Success>(state)
        assertEquals(2, state.components.size)
        assertEquals("Updated Label", state.components[0].label)
    }
}

/**
 * Mock repository implementation for local unit test coverage.
 */
class FakeDynamicUiRepository(
    var result: List<UiComponent> = emptyList(),
    var shouldThrow: Boolean = false
) : DynamicUiRepository {
    override suspend fun getComponentConfiguration(): List<UiComponent> {
        if (shouldThrow) {
            throw Exception("Failed to fetch config")
        }
        return result
    }
}
