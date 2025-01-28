
# Jetpack Compose Android Project

## Overview

This project is a modern Android application built using Jetpack Compose, Kotlin, and the latest Android development practices.

## Project Structure

The project is structured as follows:

- `app/`: Contains the main application code and resources
- `src/main/`: Main source code for the application
- `src/androidTest/`: Android instrumentation tests
- `src/test/`: Unit tests (non UI tests)

## Main Application

### Pages

Our application consists of the following main pages:

1. **Home Page**
    displays a list of items fetched from a ViewModel. The list is populated using a LazyColumn. Each item in the list is a Text view. The screen also includes a Button that allows adding new items to the list.

2. **XML Screen**
    displays a custom layout defined in an XML file. The layout includes a TextView and a Button. The TextView displays a message, and the Button updates the TextView's text when clicked.

3. **Web Screen**
    displays a WebView that loads a specified URL. The WebView loads the Android Compose documentation website by default. It allows users to navigate to other websites.

4. **Permissions Screen**
    demonstrates requesting a specific permission (in this case, Camera permission). The screen includes a Button that, when clicked, requests the Camera permission. The user is prompted to grant or deny the permission. The result of the permission request is displayed as a Toast message.vigate to other websites.

5. **[All Components Screen]**
   The `AllComponentsScreen()` is a Composable function that showcases various UI components and widgets available in Jetpack Compose. It uses a `LazyColumn` to display different sections, each demonstrating a specific category of UI elements. Here's a breakdown of the sections:

      1. Text & Display
      2. Input Fields
      3. Buttons
      4. Selection Controls
      5. Scrollable Containers (commented out)
      6. Lists and Grids
      7. Images
      8. Modals & Popups
      9. Navigation (commented out)
      10. Sliders and Pickers
      11. Progress Indicators
      12. Containers
      13. Tabs and Steppers
      14. Chips (commented out)
      15. Gestures
      16. Graphics
      17. Animations
      18. Advanced Widgets
      19. Platform Views (commented out)

## Dependencies

The dependencies used in the project are as follows:

- `junit`: JUnit testing framework for unit testing the application's Java code.
- `androidx.test.ext`: JUnit testing framework for instrumented tests in the Android application.
- `androidx.test.espresso`: Espresso testing framework for UI testing in the Android application.
- `androidx.compose.ui`: Jetpack Compose UI library for building UI components.
- `org.mockito:mockito-android`: Mockito Android library for mocking dependencies in instrumented tests.
- `org.mockito.kotlin:mockito-kotlin`: Mockito Kotlin library for mocking dependencies in instrumented tests using Kotlin.
- `io.mockk:mockk-android`: MockK Android library for mocking dependencies in instrumented tests.

## Android Tests

### Overview

The Android tests for this project are located in the `app/src/androidTest/` directory. These tests are designed to verify the functionality and UI of the application on actual Android devices or emulators.