plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  id("kotlin-parcelize")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.getstoryteller.storytellersampleapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.getstoryteller.storytellersampleapp"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      storeFile = rootProject.file("keystore.jks")
      storePassword = "password"
      keyAlias = "alias"
      keyPassword = "password"
    }
  }

  buildTypes {
    named("release") {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
    }

    named("debug") {
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
    }
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.2"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  val storytellerVersion = "9.8.2"

  implementation(group = "Storyteller", name = "sdk", version = storytellerVersion)

  implementation(platform("androidx.compose:compose-bom:2023.10.01"))

  implementation ("androidx.fragment:fragment-ktx:1.6.2")
  implementation("androidx.activity:activity-compose:1.8.1")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material:material")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
  implementation("androidx.navigation:navigation-compose:2.7.5")
  implementation("androidx.compose.runtime:runtime:1.5.4")
  implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.10.0")

  // Network, Serialization and Logging
  val ktorVersion = "2.3.6"
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

  implementation("com.jakewharton.timber:timber:5.0.1")

  // DI Hilt
  implementation("com.google.dagger:hilt-android:2.48")
  kapt("com.google.dagger:hilt-android-compiler:2.48")

  /**
   * GAM for Storyteller Ads
   */
  implementation("com.google.android.gms:play-services-ads:22.5.0")
}
