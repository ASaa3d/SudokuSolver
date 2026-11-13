# <img src="media/logo.png" alt="" width="48" align="center"> Sudoku Solver

A cross-platform Sudoku application built to demonstrate end-to-end app and web development: shared Kotlin code, native Android integration, a browser app, authentication, backend communication, and on-device machine-learning inference.

The app lets you capture or upload a Sudoku puzzle, recognize its grid, enter and edit digits, ask for hints, and solve the puzzle. The interface adapts across phone and larger screens.

## Try it

- **Web app:** [sudoku-solver-5jf.pages.dev](https://sudoku-solver-5jf.pages.dev)
- **Android app:** Download an APK from this repository's [Releases](https://github.com/ASaa3d/SudokuSolver/releases).
- **Android App Links:** Verification and password-reset links can open the Android app when it is installed. The web app also handles these routes.

## Demo

<!-- TODO: Replace this placeholder with a link or embed after uploading the demo video. -->

**Demo video:** [Add demo video here](#)

## Screenshots

### Sudoku board

| Web — wide layout | Web — compact layout |
|---|---|
| ![Sudoku board in a wide web layout](media/Pasted%20image.png) | ![Sudoku board in a compact layout](media/Pasted%20image%20%282%29.png) |

![Sudoku board and controls in a wide layout](media/Pasted%20image%20%283%29.png)

### Authentication

| Sign up — wide layout | Sign up — compact layout |
|---|---|
| ![Sign-up screen in a wide layout](media/Pasted%20image%20%284%29.png) | ![Sign-up screen in a compact layout](media/Pasted%20image%20%285%29.png) |

![Password-reset screen](media/Pasted%20image%20%286%29.png)

### Email and completion

| Password-reset email | Completion message |
|---|---|
| ![Password-reset email](media/Pasted%20image%20%287%29.png) | ![Password reset success screen](media/Pasted%20image%20%288%29.png) |

## Features

- Create an account, log in, or continue as a guest.
- Verify accounts and reset passwords using email codes or links.
- Capture a puzzle with the Android camera or choose an image from the gallery.
- Recognize Sudoku grids on Android using locally integrated machine-learning models; web image processing uses the backend.
- Work with recognized puzzles, enter and erase digits, undo moves, request logical hints, and solve the board.
- Use responsive Compose layouts on Android and in the browser.
- Open supported account links in the Android app through verified Android App Links.
- Store authentication and app preferences using platform-backed DataStore implementations.

## Technology

- **Kotlin Multiplatform** shares application logic across Android and web.
- **Compose Multiplatform** provides the user interface.
- **Kotlin/JS and Kotlin/Wasm** build the web application.
- **Ktor** handles HTTP requests and JSON serialization.
- **Koin** wires repositories, use cases, and view models.
- **Navigation 3** manages screens and back stacks; the web target synchronizes navigation with browser history.
- **DataStore** persists local preferences and authentication data.
- **ONNX models and a compiled C++ library** power Android's on-device Sudoku recognition.
- **Spring Boot** powers the backend used by both Android and web clients.

## Architecture

The project separates platform entry points from shared application code:

| Module/source set | Responsibility |
|---|---|
| `androidApp` | Android application shell, manifest, app links, and release packaging. |
| `webApp` | Browser entry point and web packaging/deployment. |
| `shared/src/commonMain` | Shared Compose UI, navigation, feature layers, domain logic, networking, and resources. |
| `shared/src/androidMain` | Android-specific implementations, including camera/image support, DataStore, and the native Sudoku recognizer bridge. |
| `shared/src/webMain` | Browser-specific implementations, including browser history synchronization and web storage. |
| `shared/src/jsMain`, `shared/src/wasmJsMain` | Target-specific platform declarations and browser builds. |

### Authentication example: from form to backend

The login flow is an example of how a feature is split into layers and connected:

```text
LoginScreenCompactLayout
        │ user input and actions
        ▼
LoginScreen ── observes state and collects navigation events
        │
        ▼
LoginViewModel ── validates UI state, calls the use case, emits LoginEvent
        │
        ▼
LoginUseCase ── applies domain rules
        │
        ▼
AuthRepository (domain contract)
        │ implemented by
        ▼
AuthRepositoryImpl ── coordinates remote calls and local token storage
        ├── AuthRemoteDataSourceImpl ── Ktor request and response DTOs
        └── LocalDataSourceImpl ── DataStore persistence
```

In the code, the primary files are:

- **Presentation:** [`LoginScreen.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/presentation/LoginScreen.kt), [`LoginScreenCompactLayout.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/presentation/view/LoginScreenCompactLayout.kt), [`LoginState.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/presentation/state/LoginState.kt), and [`LoginEvent.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/presentation/event/LoginEvent.kt).
- **View model and domain:** [`LoginViewModel.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/presentation/viewmodel/LoginViewModel.kt), [`LoginUseCase.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/domain/usecase/LoginUseCase.kt), [`LoginData.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/domain/model/LoginData.kt), and [`AuthRepository.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/domain/repository/AuthRepository.kt).
- **Data:** [`AuthRepositoryImpl.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/data/repository/AuthRepositoryImpl.kt), [`AuthRemoteDataSource.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/data/remote/AuthRemoteDataSource.kt), and [`AuthRemoteDataSourceImpl.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/data/remote/AuthRemoteDataSourceImpl.kt).
- **Dependency injection:** [`AuthModule.kt`](shared/src/commonMain/kotlin/com/example/sudokusolver/auth/di/AuthModule.kt) binds implementations and constructs use cases and view models.

On login, the screen forwards user input to the view model. The view model invokes the use case, which delegates to the domain repository contract. The data implementation calls the remote source and saves the returned token through the local data source. The result updates observable state or emits an event; the screen observes that event and navigates to the next screen. Sign-up, verification, and password reset follow the same general boundaries.

### Sudoku recognition and native inference

Two self-implemented ONNX models are used together in the Android recognition pipeline:

1. A grid-detection model locates the Sudoku board in an image.
2. A CNN-based digit-recognition model classifies the digits in the detected cells.

On Android, `AndroidSudokuRecognizer` passes the model files and image input to `MinSud`, which calls the compiled C++ mobile library through JNI. This keeps inference on device and avoids sending the captured image to the server for recognition. The Kotlin `SudokuRecognizer` interface allows the Sudoku repository to use the Android implementation while keeping the feature boundary in shared code.

In the browser, the app sends the image through the backend-based recognition path instead of invoking the Android native library.

- **ONNX models / native C++ library repository:** [ASaa3d/SudokuSolver_LIB](https://github.com/ASaa3d/SudokuSolver_LIB)
- **Backend repository:** [ASaa3d/SudokuSolver_Backend](https://github.com/ASaa3d/SudokuSolver_Backend)

## Backend and service notes

Both Android and web clients use a **Spring Boot** backend for account operations and server-side features. The backend runs on a small free instance, so it may take a little while to wake up when the app first contacts it.

Email delivery uses a free SMTP service and can occasionally be delayed or missed. This can affect whether an email arrives promptly, but does not otherwise affect the app's functionality.

## Run locally

Requirements: a compatible JDK, Android SDK for Android builds, and a supported browser for web builds.

```bash
# Build the Android debug app
./gradlew :androidApp:assembleDebug

# Run the web app with Kotlin/Wasm
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# Or run the Kotlin/JS web target
./gradlew :webApp:jsBrowserDevelopmentRun
```

The shared module reads service configuration from environment variables or `local.properties`. Configure the relevant development values (`BASE_URL_DEV`, `DL_BASE_URL_DEV`, and `IMG_API_KEY`) before running against your own services. Production builds use the corresponding production URL settings. Do not commit credentials or API keys.

## Tests

```bash
./gradlew :shared:testAndroidHostTest
./gradlew :shared:jsTest
./gradlew :shared:wasmJsTest
```
