bash gradlew :app:assembleRelease installRelease
adb shell am start -n "com.getstoryteller.storytellersampleapp/com.getstoryteller.storytellersampleapp.features.main.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER

TODAYS_DATE=$(date +%Y%m%d)
if [ "$1" == "zip" ]; then
    ZIP_NAME="Showcase-apk-nightly-$TODAYS_DATE.zip"
    APK_NAME="Showcase-apk-nightly-$TODAYS_DATE.apk"
    cp "./app/build/outputs/apk/release/app-release.apk" "./$APK_NAME"
    zip "./$ZIP_NAME" "./$APK_NAME"
    rm "./$APK_NAME"
    open .
fi
