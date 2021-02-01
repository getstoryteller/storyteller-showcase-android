# Storyteller Android

To build the sample app:
1. Update `gradle.properties` with your SDK License Key by replacing `[LICENSEKEY]` with the correct value (please refer to our guide on [Getting Started](https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#how-to-add-the-sdk-to-your-project) for more details)
2. Run a Gradle Sync from within Android Studio
3. Supply your app's API Key in `AndroidManifest.xml` by replacing `[APIKEY]` with the correct value (please refer to our guide on [Getting Started](https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#-1) for more details). Alternatively, remove the `StorytellerKey` manifest entry, uncomment and use the `Storyteller.initialize("[APIKEY]")` method in `MainActivity.kt` with an API Key.