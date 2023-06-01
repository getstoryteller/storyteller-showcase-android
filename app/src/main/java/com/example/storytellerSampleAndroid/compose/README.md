# Storyteller Compose SDK

<div><img src="art/compose.png" align="right" alt="Jetpack Compose Logo" height="120" style="margin-left: 1%;margin-right: 1%;"></div>
<div><img src="art/storyteller.png" align="right" alt="Storyteller Logo" height="120" style="margin-left: 1%;margin-right: 1%;"></div>

Storyteller is now speaking compose. Designed to fulfill all your needs and suites well in the compose ecosystem.

With an elegant API optimized to work well inside your compose applications, Storyteller Compose SDK is the perfect companion for your next compose project.

<div><img src="art/usage-example.png" alt="Storyteller Compose SDK Usage Example"></div>

## Supported Views
- [x] `StorytellerRowView` a composable to displays row of stories
- [x] `StorytellerGridView` a composable to displays grid of stories
- [x] `StorytellerClipsRowView` a composable to displays row of clips
- [x] `StorytellerClipsGridView` a composable to displays grid of clips


## Usage

1. Using composables directly

    ```kotlin
      StorytellerRowView(
        modifier = modifier,
        tag = type.tag, // tag to identify this composable
        controller = controller // controller to control the state of the composable mainly we should define one per activity
      ) { // the configuration block
        delegate = listViewDelegate
        cellType = StorytellerListViewCellType.SQUARE
        categories = // provide the categories
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
   // or you can specifiy a list of views (using their tags) to do anything on them
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
