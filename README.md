# chuck-norris-creates-android

## About the project

It is a Android application that allows the user search for Chuck Norris Facts.
It uses the `api.chucknorris.io` to retrieve the Facts.

### Continuous Integration

The project uses Github Actions and Firebase Test Lab to build and test the application.

## Running the project in the Android Studio

The Android Studio version used was Android Studio 4.2.0 Beta 1 this
means that is using Android Gradle Plugin `4.2.0-beta01`, if you are using a lower android studio version
you should downgrade the Android Gradle Plugin to your Android Studio version in the `buildSrc/build.gradle.kts`.

## Building the project with CLI

Run the command: `./gradlew assembleDebug`

The apk will be generated to `android/build/outputs/apk/debug`

## Illustration

![](https://media.giphy.com/media/SWSTrCNksuVdYDI9vr/giphy.gif)

## Modules

- chucknorrisfacts-api: Module that contains the API calls for the `https://api.chucknorris.io/`.
It uses Kotlin Multiplatform to be a module independent of platform adding possibility to use it on a iOS app. 
It uses [Ktor Client](https://ktor.io/docs/client.html) as a network library and it uses [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for parsing JSON.

- repository: Is a Repository Pattern module, also Kotlin Multiplatform, currently, it's only uses **Network Source**(chucknorrisfacts-api), but if the project requires Local Source(SQLDelight) is painless to implement.

- android: The android application allows the user to search for chuck norris facts (from `https://api.chucknorris.io/`) and share the facts.

## Project decisions

The project uses a reactive approach for handling search results, it uses Kotlin Coroutines Flow to do so.
The project it is a little modular because it modules should be unbundled from Android API and should be Kotlin Multiplatform.
