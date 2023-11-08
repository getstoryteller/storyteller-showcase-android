# Storyteller Compose

<a href="https://getstoryteller.com" target="_blank">
  <img alt="Storyteller integration examples for Android, from getstoryteller.com" src="/img/readme-cover.png">
</a>

![Gradle Compatible](https://img.shields.io/badge/Gradle%20Compatible-green?logo=gradle) [![Main SDK](https://badgen.net/github/tag/getstoryteller/storyteller-sample-android?label=latest+release&)](https://github.com/getstoryteller/storyteller-sample-android/tags) ![Compose SDK](https://img.shields.io/badge/dynamic/json?color=blue&label=Compose%20SDK&query=name&url=https://api.razonyang.com/v1/github/tag/getstoryteller/storyteller-sample-android%3Fprefix=compose-)

<p>
  <a href="https://getstoryteller.com" target="_blank"><img alt="What is Storyteller?" src="/img/Storyteller-Btn-Default.png" height="40"></a>
  <a href="https://docs.getstoryteller.com/documents/android-sdk" target="_blank"><img alt="Storyteller Android Documentation" src="/img/Android-Documentation-Btn-Default.png" height="40"></a>
  <a href="https://www.getstoryteller.com/documentation/android/storyteller-compose" target="_blank"><img alt="Storyteller Compose Documentation" src="/img/JetpackCompose-Btn-Active.png" height="40"></a>
</p>

Storyteller is now speaking compose. Designed to fulfill all your needs and suites well in the compose ecosystem.

With an elegant API optimized to work well inside your compose applications, Storyteller Compose SDK is the perfect companion for your next compose project.

## Supported Views
- [x] `StorytellerStoriesRow` a composable to displays row of stories
- [x] `StorytellerStoriesGrid` a composable to displays grid of stories
- [x] `StorytellerClipsRow` a composable to displays row of clips
- [x] `StorytellerClipsGrid` a composable to displays grid of clips


## Usage

Using composables directly

```kotlin
   StorytellerStoriesRow(
      modifier = Modifier,
      dataModel = StorytellerStoriesDataModel(categories = emptyList()), // data model with the configuration for your Composables.
      delegate = listViewDelegate, // delegate for the Composables. 
      isRefreshing = false, // a Boolean flag that if true, will trigger data reload.
   )
```

For more information, visit the documentation: https://www.getstoryteller.com/documentation/android/storyteller-compose

## Download

![Latest Version](https://img.shields.io/badge/dynamic/json?color=blue&label=Compose%20SDK&query=name&url=https://api.razonyang.com/v1/github/tag/getstoryteller/storyteller-sample-android%3Fprefix=compose-)

```groovy
repositories {
   maven {
      url 'https://www.myget.org/F/storyteller-android-sdk/maven/'
   }
}

dependencies {
    implementation 'Storyteller:sdk-compose:<latest-version>'
}
```
