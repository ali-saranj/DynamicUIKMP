# AI-Usage-Report

This report details the integration of AI assistance during the architectural design and development of the **DynamicUI-KMP** Server-Driven UI system.

---

## 🧭 1. Architectural Guidance & Layer Scaffolding
AI was utilized to design a highly decoupled **Clean Architecture** package layout that satisfies Separation of Concerns. 

*   **Prompting Approach**: We prompted the AI to draft an ASCII package layout separating network logic (Ktor, kotlinx.serialization) from Jetpack Compose and business use cases.
*   **Resulting Solution**: The AI proposed a detailed layout that isolates:
    *   **Data**: DTOs, Ktor Client, and Repository implementations.
    *   **Domain**: Use cases and pure business entity models (`UiComponent.kt`).
    *   **Presentation**: ViewModels driving flows.
    *   **UI**: Declarative Jetpack Compose dynamic rendering components.

---

## 🛡️ 2. Custom Polymorphic Serializer & Fallback Design
One of the key engineering challenges in Server-Driven UI is avoiding serialization crashes when the backend updates the API schema with unrecognized UI component types. 

We used AI to design a custom JSON serializer using `kotlinx.serialization`.

### Example AI Prompt:
> *"Write a custom polymorphic deserializer in Kotlin Multiplatform using kotlinx.serialization for a list of components where components differ by a `type` string. If the type is unknown to the client, instead of throwing a serialization exception, deserialize it safely into an UnknownComponent subclass containing the original type name."*

### AI Output & Refactoring:
The AI drafted a serializer extending `JsonContentPolymorphicSerializer<ComponentDto>`:
```kotlin
object ComponentDtoSerializer : JsonContentPolymorphicSerializer<ComponentDto>(ComponentDto::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ComponentDto> {
        val jsonObject = element.jsonObject
        val type = jsonObject["type"]?.jsonPrimitive?.content
        return when (type) {
            "text_input" -> TextFieldDto.serializer()
            "number_input" -> NumberInputDto.serializer()
            "slider" -> SliderDto.serializer()
            else -> UnknownComponentDto.serializer() // Safely fall back
        }
    }
}
```
This output was reviewed and integrated directly. We then refactored the domain mapping to ensure that `UnknownComponentDto` resolves to `UiComponent.Unsupported`, allowing Compose to render a clean warning card instead of crashing the screen.

---

## 🛠️ 3. Platform Conflicts & Refactoring (Human-in-the-Loop)
During the integration, two major platform issues were encountered and resolved:

### A. KMP String Formatting Issue (iOS Compilation Error)
*   **Conflict**: The initial draft of `SliderComponent.kt` used `String.format("%.1f", sliderValue)`. This is a JDK-specific method that fails to compile on Kotlin/Native targets like iOS.
*   **Resolution**: We refactored the formatting to use a KMP-safe float rounding extension:
    ```kotlin
    private fun String.Companion.format(format: String, vararg args: Any?): String {
        val value = args[0] as Float
        val multiplier = 10 // Round to 1 decimal place
        val rounded = (value * multiplier).roundToInt().toFloat() / multiplier
        return rounded.toString()
    }
    ```
    This successfully bypassed JDK dependencies and allowed multi-platform builds to pass.

### B. Network Proxy Resolution Issues
*   **Conflict**: The local Gradle compilation failed to download Maven artifacts due to TLS handshake timeouts on proxy properties configured in `gradle.properties`.
*   **Resolution**: We temporarily commented out the proxy settings, successfully downloaded and cached the required library JARs, and restored the original proxy configs to ensure the developer's workspace settings remained intact.

---

## 🧪 4. Testing & Validation Verification
AI helped generate unit test boilerplate for both the data and presentation layers:
*   [ComponentSerializationTest.kt](file:///E:/AndroidProject/DynamicUIKMP/shared/src/commonTest/kotlin/com/example/dynamicui/data/model/ComponentSerializationTest.kt): Validates JSON parsing and guarantees that unknown components fall back smoothly to `UnknownComponentDto` without throwing serialization exceptions.
*   [DynamicUiViewModelTest.kt](file:///E:/AndroidProject/DynamicUIKMP/shared/src/commonTest/kotlin/com/example/dynamicui/presentation/DynamicUiViewModelTest.kt): Uses a fake repository to mock loading transitions (`Loading -> Success` or `Loading -> Error`) and processes View Intents inside Coroutines.

All unit tests compiled and passed successfully under:
```bash
./gradlew :shared:testAndroidHostTest
```
