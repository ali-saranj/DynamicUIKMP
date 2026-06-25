package com.example.dynamicui.domain.model

/**
 * Domain representation of components rendering dynamically on screen.
 * This is decoupled from any network protocols or JSON libraries.
 */
sealed interface UiComponent {
    val id: String
    val label: String

    /**
     * Text Input Component representation.
     */
    data class TextInput(
        override val id: String,
        override val label: String,
        val placeholder: String,
        val isRequired: Boolean
    ) : UiComponent

    /**
     * Number Input Component representation.
     */
    data class NumberInput(
        override val id: String,
        override val label: String,
        val min: Int,
        val max: Int
    ) : UiComponent

    /**
     * Slider Input Component representation.
     */
    data class SliderInput(
        override val id: String,
        override val label: String,
        val min: Float,
        val max: Float,
        val defaultValue: Float,
        val step: Float
    ) : UiComponent

    /**
     * Fallback representation for unknown/unsupported components.
     */
    data class Unsupported(
        override val id: String,
        val typeName: String
    ) : UiComponent {
        override val label: String = "Unsupported ($typeName)"
    }
}
