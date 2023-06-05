# Storyteller Compose

<a href="https://getstoryteller.com" target="_blank">
  <img alt="Storyteller integration examples for Android, from getstoryteller.com" src="/img/readme-cover.png">
</a>

![Gradle Compatible](https://img.shields.io/badge/Gradle%20Compatible-green?logo=gradle) [![Main SDK](https://badgen.net/github/tag/getstoryteller/storyteller-sample-android?label=latest+release&)](https://github.com/getstoryteller/storyteller-sample-android/tags) ![Compose SDK](https://img.shields.io/badge/dynamic/json?color=blue&label=Compose%20SDK&query=name&url=https://api.razonyang.com/v1/github/tag/getstoryteller/storyteller-sample-android%3Fprefix=compose-)

<p>
  <a href="https://getstoryteller.com" target="_blank"><img alt="What is Storyteller?" src="/img/Storyteller-Btn-Default.png" height="48"></a>&nbsp;&nbsp;&nbsp;
  <a href="https://docs.getstoryteller.com/documents/android-sdk" target="_blank"><img alt="Storyteller Android Documentation" src="/img/Android-Documentation-Btn-Default.png" height="48"></a>
  <a href="https://www.getstoryteller.com/documentation/android/storyteller-compose" target="_blank"><img alt="Storyteller Compose Documentation" src="/img/JetpackCompose-Btn-Active.png" width="329" height="48"></a>
</p>

Storyteller is now speaking compose. Designed to fulfill all your needs and suites well in the compose ecosystem.

With an elegant API optimized to work well inside your compose applications, Storyteller Compose SDK is the perfect companion for your next compose project.

<div><img src="/img/usage-example.png" alt="Storyteller Compose SDK Usage Example"></div>

## Supported Views
- [x] `StorytellerStoriesRowView` a composable to displays row of stories
- [x] `StorytellerStoriesGridView` a composable to displays grid of stories
- [x] `StorytellerClipsRowView` a composable to displays row of clips
- [x] `StorytellerClipsGridView` a composable to displays grid of clips


## Usage

1. Using composables directly

    ```kotlin
      StorytellerStoriesRowView(
        modifier = modifier,
        tag = type.tag, // tag to identify this composable
        controller = controller // controls the state of the composable mainly we should define one per activity
      ) { // the configuration block
        delegate = listViewDelegate
        cellType = StorytellerListViewCellType.SQUARE
        categories = listOf("category-1") // provide the categories
        reloadData() // we want to reload data when the backing view is initialized
      }
    ```

2. Defining the controller
    ```kotlin
    class MainActivity : ComponentActivity() {
   
        private lateinit var controller: StorytellerComposeController

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // ... 
            controller = StorytellerComposeController().bind(this)
            // ...
        
        }
    }
    ```

Don't worry about doing anything in the `onDestroy` method, the controller will handle it for you.

3. Doing something on the Storyteller Composables
   You can always access the composables at any time using the `controller` instance. For example, if you want to reload the data of a `StorytellerRowView` you can do the following:

   ```kotlin
   controller.reloadData() // reload all the composables in the current controller
   // or you can specify a list of views (using their tags) to do anything on them
   // controller.forEach("tag-1", "tag-2") { view ->
       // do something on the view  
   }
    ```

For more information, visit the documentation: https://www.getstoryteller.com/documentation/android/storyteller-compose

## Download

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
