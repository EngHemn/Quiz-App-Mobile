# Quiz App Project Rules & Architecture Specification

> [!IMPORTANT]
> All AI agents and developers working on this project must read and strictly adhere to these rules. No exceptions are allowed.

---

## 1. Dependency Injection — REQUIRED

* Dependency Injection **must** be used throughout the project.
* Use **Hilt** for Dependency Injection.
* Do not manually create repositories, use cases, data sources, or ViewModels inside Compose.
* Dependencies must be provided through DI modules.
* ViewModels must receive their dependencies through constructor injection.
* Repository implementations must be injected behind Domain repository interfaces.
* Data sources must be injected.
* Avoid service locators and global mutable singletons.

### Architecture Flow:
```text
Compose
   ↓
ViewModel
   ↓
UseCase
   ↓
Repository Interface
   ↓
Repository Implementation
   ↓
DataSource
```
*Hilt connects these dependencies.*

---

## 2. Coil — REQUIRED for Images

* Use **Coil** for image loading in Jetpack Compose.
* Do not use manual image downloading inside UI.
* Use `AsyncImage`:
  ```kotlin
  AsyncImage(
      model = imageUrl,
      contentDescription = null
  )
  ```
* **Image Loading Flow:**
  ```text
  UI
   ↓
  Coil
   ↓
  Image URL / local image
  ```
* Image-related responsibilities must stay outside business logic. (e.g., do not put image downloading into a Use Case).

---

## 3. Design Patterns — REQUIRED (When Appropriate)

The project must use established design patterns instead of putting everything into a single class. Introduce patterns only when they solve an actual problem to avoid unnecessary abstraction.

* **MVVM** — Presentation architecture.
* **Clean Architecture** — Layer separation.
* **Repository Pattern** — Data abstraction.
* **Use Case Pattern** — Business operations.
* **Dependency Injection** — Dependency management.
* **Observer / Reactive Pattern** — `StateFlow`, `Flow`.
* **Factory Pattern** — Object creation where appropriate.
* **Strategy Pattern** — Replaceable algorithms/business strategies where appropriate.
* **Adapter Pattern** — Converting external/data models when needed.
* **Mapper Pattern** — DTO ↔ Domain ↔ UI models.
* **Facade Pattern** — Simplifying complex subsystem access when useful.
* **Builder Pattern** — Only where complex object construction requires it.

---

## 4. Model Separation — REQUIRED

Do not use the same model everywhere. Use separate models and map between layers:

```text
data/model (DTO) ──[ Mapper ]──> domain/model ──[ Mapper ]──> presentation/model (UI Model)
```
*Example:* `ProductDto` ➔ `Product` ➔ `ProductUiModel`.
This prevents API or database structures from leaking into the Domain or UI.

---

## 5. Compose Rules — STRICT

Composable functions must be **UI-focused**.

* **They should:**
  * Receive state.
  * Display state.
  * Send events.
  * Call callbacks.
  * Render components.
* **They must NOT:**
  * Call repositories.
  * Call APIs.
  * Access Room.
  * Access Firebase.
  * Perform business calculations.
  * Perform authentication logic.
  * Contain complex validation.
  * Create dependencies manually.
  * Contain database logic.

*Example:*
```kotlin
@Composable
fun ProductScreen(
    state: ProductUiState,
    onEvent: (ProductEvent) -> Unit
) {
    // UI only
}
```

---

## 6. ViewModel Rules — STRICT

Every feature/screen that needs state should have a ViewModel.
* **Structure:** `ProductScreen` ➔ `ProductViewModel` ➔ `ProductUiState` ➔ `ProductEvent`.
* **Flow:**
  ```text
  UI Event ➔ ViewModel ➔ UseCase ➔ State update ➔ Compose
  ```
* **State Collection:**
  ```kotlin
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  ```
  Always use lifecycle-aware state collection.

---

## 7. State Management

* Use immutable UI state:
  ```kotlin
  data class ProductUiState(
      val products: List<ProductUiModel> = emptyList(),
      val isLoading: Boolean = false,
      val error: String? = null
  )
  ```
* Do not expose mutable state directly from ViewModels.
* Avoid having multiple unrelated mutable states scattered throughout the screen. Prefer one clear state model per screen.

---

## 8. Events

Use a single event flow for complex screens:
```kotlin
sealed interface ProductEvent {
    data object Load : ProductEvent
    data class SearchChanged(val value: String) : ProductEvent
    data class ProductClicked(val id: String) : ProductEvent
    data object Retry : ProductEvent
}
```
* **Compose:** `onEvent(ProductEvent.Retry)`
* **ViewModel:** `onEvent() ➔ business/presentation logic ➔ UseCase ➔ update state`

---

## 9. Domain Layer — ZERO Framework Dependency

The Domain layer must remain pure Kotlin. It must **not** depend on:
* Android, Compose, Material 3, Hilt, Coil, Retrofit, Room, Firebase, ViewModel, Activity, or Context.
* **Domain contains:**
  ```text
  domain/
  ├── model/
  ├── repository/
  └── usecase/
  ```
* This is a **hard rule**.

---

## 10. Material 3 — REQUIRED

The entire UI must use **Jetpack Compose + Material 3**.
* Centralize all theme resources under:
  ```text
  theme/
  ├── Color.kt
  ├── Type.kt
  ├── Shape.kt
  └── Theme.kt
  ```
* Use `MaterialTheme.colorScheme`, `MaterialTheme.typography`, and `MaterialTheme.shapes`.
* Do not hardcode design values throughout screens.

---

## 11. Final Architecture Summary

```text
┌──────────────────────────────┐
│       Presentation           │
│                              │
│ Compose + Material 3        │
│ Screen                       │
│ Components                   │
│ ViewModel                    │
│ UiState                      │
│ Events                       │
└──────────────┬───────────────┘
               │
               ↓
┌──────────────────────────────┐
│           Domain             │
│                              │
│ Models                       │
│ Use Cases                    │
│ Repository Interfaces        │
│                              │
│ NO Android                    │
│ NO Compose                    │
│ NO Hilt                       │
│ NO Coil                       │
│ NO Room                       │
└──────────────┬───────────────┘
               ↑
               │
┌──────────────┴───────────────┐
│            Data              │
│                              │
│ Repository Implementations   │
│ Data Sources                 │
│ DTOs                         │
│ Mappers                      │
│ Room / Retrofit / Firebase   │
└──────────────────────────────┘

              Hilt
        ↓      ↓      ↓
   ViewModel  UseCase  Repository
```

### The Golden Rule (Never allow this):
```text
Compose ➔ Repository ❌
Compose ➔ API ❌
Compose ➔ Room ❌
Compose ➔ Firebase ❌
ViewModel ➔ Repository Implementation ❌
Domain ➔ Android ❌
Domain ➔ Hilt ❌
Domain ➔ Compose ❌
```
