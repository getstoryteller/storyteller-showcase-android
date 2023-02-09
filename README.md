# Storyteller Android Sample App

<a href="https://getstoryteller.com" target="_blank">
  <img alt="Storyteller integration examples for Android, from getstoryteller.com" src="img/readme-cover.png">
</a>

![Gradle Compatible](https://img.shields.io/badge/Gradle-Compatible-green?logo=gradle) [![Latest Release](https://badgen.net/github/tag/getstoryteller/storyteller-sample-android?label=latest+release)](https://github.com/getstoryteller/storyteller-sample-android/tags)

<p>
  <a href="https://getstoryteller.com" target="_blank"><img alt="What is Storyteller?" src="img/what-is-storyteller-btn.png" width="302" height="48"></a>&nbsp;&nbsp;&nbsp;
  <a href="https://docs.getstoryteller.com/documents/android-sdk" target="_blank"><img alt="Storyteller Android Documentation" src="img/docs-btn.png" width="329" height="48"></a>
</p>

Use this repo as a reference for integrating Storyteller in your Android App.

Storyteller is also available for [iOS](https://github.com/getstoryteller/storyteller-sample-ios)
, [React Native](https://github.com/getstoryteller/storyteller-sdk-react-native)
and [Web](https://github.com/getstoryteller/storyteller-sample-web).

For help with Storyteller, please check
our [Documentation and User Guide](https://docs.getstoryteller.com/documents/) or
contact [support@getstoryteller.com](mailto:support@getstoryteller.com?Subject=Android%20Sample%20App)
.

## Building the Sample App

The sample app can be built with Android Studio and should work out of the box with default content.
If you want to use your own API KEY, then supply your app's API Key in `SampleApp.kt` by
replacing `[APIKEY]` in `Storyteller.initialize("[APIKEY]")` with the correct value (please refer to our guide
on [Getting Started](https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#sdk-initialization)
for more details).

## Using Google Ad Manager with Storyteller

If you use Google Ad Manager as your ad server, we have provided a working sample inside the app.
You can of course use your own GAM config.

### Configuration

To use the library, first configure it with the correct values from GAM:

 ```xml

<manifest>
    <application>
        <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="[GAM APP ID]" />
    </application>
</manifest>
 ```

You also need to supply a format id and unit id in `StorytellerAdsDelegate.kt`:

```kotlin
    companion object {
  val adUnitId = [AdUnitId]
  val formatId = [NativeAdFormatId]
}
```

where:

- `NativeAdFormatId` is the ID of the Native Ad Format
- `AdUnitId` is the Ad Unit set to traffic creative using the Native Ad Format

Steps above should be sufficient to see ads with you own GAM config.

### Connect to the Storyteller SDK

Connect the `getAdsForList` callback to get the Native ads:

```kotlin
override fun getAdsForList(
  listDescriptor: ListDescriptor,
  stories: List<ClientStory>,
  onComplete: (AdResponse) -> Unit,
  onError: () -> Unit
) {
  ...
  storytellerScope.launch {
    nativeAdsManager.requestAd(
      adUnit = adUnitId,
      formatId = formatId,
      customMap = customMap,
      onAdDataLoaded = {},
      onAdDataFailed = {}
    )
  }
}
}
```

### Ensure Ad Tracking behaves correctly

Now connect the `onUserActivityOccurred` method to check if ad events are fired:

 ```kotlin
override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
  when (type) {
    UserActivity.EventType.AD_ACTION_BUTTON_TAPPED -> onAdAction(data)
    UserActivity.EventType.OPENED_AD -> onAdStart(data)
    UserActivity.EventType.FINISHED_AD -> onAdEnd(data)
    else -> { /*no op*/
    }
  }
  ...
}
 ```
