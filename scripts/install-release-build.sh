bash gradlew :app:assembleRelease installRelease
adb shell am start -n "com.getstoryteller.storytellersampleapp/com.getstoryteller.storytellersampleapp.features.main.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
