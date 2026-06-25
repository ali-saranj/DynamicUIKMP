# DynamicUI-KMP

DynamicUI-KMP is a production-ready mobile application built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. It implements a **Server-Driven UI (SDUI)** system that fetches dynamic component configurations from a REST API and renders them dynamically on both Android and iOS.

---

## 🛠️ Tech Stack & Libraries
*   **Kotlin Multiplatform (KMP)**: Shared core logic, architecture layers, and network clients.
*   **Compose Multiplatform**: Shared declarative UI for Android and iOS.
*   **Ktor Client**: Multiplatform HTTP client handling HTTP network requests.
*   **kotlinx.serialization**: Type-safe polymorphic JSON serialization and deserialization.
*   **Kotlin Coroutines & Flow**: Reactive state flows and async task execution.

---

## 🏛️ Architectural Decisions

This project follows **Clean Architecture** and **MVI / Intent-driven State Management** to achieve strict separation of concerns, high testability, and code reliability.

```mermaid
graph TD
    subgraph UI Layer [UI Layer (Compose)]
        DynamicUiScreen --> DynamicUiRenderer
        DynamicUiRenderer --> TextFieldComponent
        DynamicUiRenderer --> NumberInputComponent
        DynamicUiRenderer --> SliderComponent
        DynamicUiRenderer --> UnknownComponent
    end

    subgraph Presentation Layer [Presentation Layer (MVI)]
        DynamicUiViewModel -->|Exposes StateFlow| DynamicUiState
        DynamicUiScreen -->|Dispatches Intents| DynamicUiIntent
        DynamicUiViewModel -.->|Processes| DynamicUiIntent
    end

    subgraph Domain Layer [Domain Layer (Core Logic)]
        FetchDynamicUiUseCase --> DynamicUiRepository
        UiComponent
    end

    subgraph Data Layer [Data Layer (Ktor & Serialization)]
        DynamicUiRepositoryImpl -->|Implements| DynamicUiRepository
        DynamicUiRepositoryImpl --> DynamicUiApi
        KtorDynamicUiApi -->|Implements| DynamicUiApi
        ComponentDto
        ComponentDtoSerializer
    end

    DynamicUiViewModel --> FetchDynamicUiUseCase
    DynamicUiRepositoryImpl -.->|Maps DTOs to| UiComponent
```

### 1. Separation of Concerns (Clean Architecture)
*   **Data Layer**: Manages the Ktor client, raw JSON schema parsing, and Data Transfer Objects (DTOs).
*   **Domain Layer**: Pure Kotlin logic containing use cases and business models (`UiComponent`). It has **zero dependencies** on JSON libraries (`kotlinx.serialization`) or HTTP clients.
*   **Presentation Layer**: Utilizes a lifecycle-aware ViewModel that processes user intents and exposes a read-only StateFlow representing the screen state.
*   **UI Layer**: Uses Jetpack Compose to draw individual components dynamically.

### 2. Polymorphic JSON Deserialization with Crash Prevention
Server-Driven UI configurations are highly dynamic. If a backend releases a new component type (e.g. `checkbox`) that the mobile client does not yet recognize, standard serializers will fail to parse and crash the screen. 
This project uses a custom `ComponentDtoSerializer` inheriting from `JsonContentPolymorphicSerializer`:
*   It checks the `type` field in the incoming JSON.
*   Recognized types (`text_input`, `number_input`, `slider`) are deserialized into their specific DTO subclasses.
*   Any unrecognized or new type is safely parsed into an `UnknownComponentDto` containing the original type name.
*   In the domain mapping layer, this is converted into `UiComponent.Unsupported`, which renders a warning fallback layout (`UnknownComponent.kt`) instead of crashing the screen.

---

## 🚀 Running the Project

### 📱 Running the Apps
*   **Android App**: Run `./gradlew :androidApp:assembleDebug` or run the `:androidApp` target from Android Studio / IntelliJ.
*   **iOS App**: Navigate to the `iosApp/` directory, open `iosApp.xcworkspace` in Xcode, and click Run.

### 🧪 Running Unit Tests
The project features a full suite of unit tests verifying polymorphic parsing, domain mapping, and ViewModel state flows.
Run the Android Host tests (which runs all `commonTest` assertions on your local machine):
```bash
./gradlew :shared:testAndroidHostTest
```

---

## 📝 Assumptions Made
1.  **Endpoint Configuration**: The default Ktor client is configured to connect to `https://api.example.com/dynamic-ui`. In real production scenarios, this URL should be injected dynamically via build variables or config engines.
2.  **State Management Scope**: The dynamic form variables are stored in memory (`remember { mutableStateMapOf() }`) at the screen level and submitted together via `DynamicUiIntent.SubmitForm`.
3.  **Local Dev Offline Cache**: The project assumes network availability for the layout config; offline backup strategies are set as future roadmap goals.

---

## 📈 Current Implementation & Future Roadmap

### ✅ What is Implemented
*   Clean Architecture layers fully decoupled.
*   Polymorphic JSON serializer with soft fallbacks on unknown types.
*   DTO to Domain entity mapping.
*   Reactive state flow driving the UI (`Idle -> Loading -> Success/Error`).
*   Material 3 Compose wrappers for text inputs, validated numeric fields, and stepping range sliders.
*   Unit tests checking serialization validity, fallback mechanisms, and ViewModel transitions.

### 🔮 Future Scalability
1.  **Dependency Injection**: Integrate **Koin** in `:shared` for clean target-specific dependency management.
2.  **Server-Driven Input Validation**: Expand the DTO layout to send validation properties (like regex patterns, min-length limits, or conditional visibility conditions) and enforce them in the `UseCase` layer.
3.  **Local Caching (Offline Mode)**: Integrate **SQLDelight** or **Room** database to store the last-successfully-fetched SDUI layout schema, providing instant offline screen rendering.