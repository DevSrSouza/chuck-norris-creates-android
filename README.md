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
