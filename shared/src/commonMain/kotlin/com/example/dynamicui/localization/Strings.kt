package com.example.dynamicui.localization

import com.example.dynamicui.getSystemLocale

/**
 * Localization dictionary supporting internationalization (i18n).
 */
sealed interface Strings {
    val dynamicForm: String
    val infoMessage: String
    val submitForm: String
    val fieldRequired: String
    val invalidInteger: String
    val mustBeBetween: String
    val unsupportedElement: String
    val unsupportedMessage: String
    val retry: String

    object English : Strings {
        override val dynamicForm = "Dynamic Form"
        override val infoMessage = "Please complete the dynamically loaded form below. Fields marked with * are required."
        override val submitForm = "Submit Form"
        override val fieldRequired = "This field is required"
        override val invalidInteger = "Must be a valid integer"
        override val mustBeBetween = "Must be between %d and %d"
        override val unsupportedElement = "Unsupported Element"
        override val unsupportedMessage = "Component with ID '%s' has type '%s' which is not supported."
        override val retry = "Retry"
    }

    object Spanish : Strings {
        override val dynamicForm = "Formulario Dinámico"
        override val infoMessage = "Complete el formulario cargado dinámicamente. Los campos marcados con * son requeridos."
        override val submitForm = "Enviar Formulario"
        override val fieldRequired = "Este campo es requerido"
        override val invalidInteger = "Debe ser un entero válido"
        override val mustBeBetween = "Debe estar entre %d y %d"
        override val unsupportedElement = "Elemento no soportado"
        override val unsupportedMessage = "El componente con ID '%s' tiene el tipo '%s' que no está soportado."
        override val retry = "Reintentar"
    }
}

/**
 * Singleton localizer checking system locale and switching string assets.
 */
object Localizer {
    val strings: Strings
        get() = when (getSystemLocale().take(2).lowercase()) {
            "es" -> Strings.Spanish
            else -> Strings.English
        }
}
