# Storyteller Android Sample App

<a href="https://getstoryteller.com" target="_blank">
  <img alt="Storyteller integration examples for Android, from getstoryteller.com" src="img/readme-cover.png" width="1200" height="784">
</a>

![Gradle Compatible](https://img.shields.io/badge/Gradle-Compatible-green?logo=gradle)

<p>
  <a href="https://getstoryteller.com" target="_blank"><img alt="What is Storyteller?" src="img/what-is-storyteller-btn.png" width="302" height="48"></a>&nbsp;&nbsp;&nbsp;
  <a href="https://docs.getstoryteller.com/documents/android-sdk" target="_blank"><img alt="Storyteller Android Documentation" src="img/docs-btn.png" width="329" height="48"></a>
</p>

Use this repo as a reference for integrating Storyteller in your Android App.

[Storyteller is also available for iOS](https://github.com/stormideas/storyteller-sample-android).

For help with Storyteller, please check our [Documentation and User Guide](https://docs.getstoryteller.com/documents/) or contact [support@getstoryteller.com](mailto:support@getstoryteller.com?Subject=Android%20Sample%20App).

## Building the Sample App

To build the sample app:
1. Update `gradle.properties` with your SDK License Key by replacing `[LICENSEKEY]` with the correct value (please refer to our guide on [Getting Started](https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#how-to-add-the-sdk-to-your-project) for more details)
2. Run a Gradle Sync from within Android Studio
3. Supply your app's API Key in `AndroidManifest.xml` by replacing `[APIKEY]` with the correct value (please refer to our guide on [Getting Started](https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#-1) for more details). Alternatively, remove the `StorytellerKey` manifest entry, uncomment and use the `Storyteller.initialize("[APIKEY]")` method in `MainActivity.kt` with an API Key.
