import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.jetbrainsKotlinKapt)
  alias(libs.plugins.jetbrainsKotlinParcelize)
  alias(libs.plugins.jetbrainsKotlinSerialization)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ktlint)
  id(libs.plugins.navigationSafeArgs.get().pluginId)
}

android {
  namespace = "com.getstoryteller.storytellershowcaseapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.getstoryteller.storytellershowcaseapp.xml"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0"
    buildConfigField("String", "API_BASE_URL", "\"${property("API_BASE_URL")}\"")
    buildConfigField("String", "AMPLITUDE_API_KEY", "\"${property("AMPLITUDE_API_KEY")}\"")
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
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
    }

    named("debug") {
      applicationIdSuffix = ".debug"
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
    }
  }
  buildFeatures {
    compose = true
    buildConfig = true
    viewBinding = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

// ktLintFormat task will need to run before preBuild
tasks.getByPath("preBuild").dependsOn("ktlintFormat")

ktlint {
  version.set(libs.versions.ktlint.get())
  android.set(true)
  ignoreFailures.set(false)
  reporters {
    reporter(ReporterType.PLAIN)
    reporter(ReporterType.CHECKSTYLE)
    reporter(ReporterType.SARIF)
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom.beta))
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.runtime)
  implementation(libs.androidx.runtime.livedata)
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  debugImplementation(libs.androidx.ui.tooling)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.navigation.ui.ktx)
  implementation(libs.navigation.fragment.ktx)
  implementation(libs.androidx.swiperefreshlayout)

  // Network, Serialization and Logging
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.client.content.negotiation)

  implementation(libs.kotlinx.serialization.json)

  implementation(libs.timber)
  implementation(libs.android.sdk)
  debugImplementation(libs.ktor.client.logging)

  // DI Hilt
  implementation(libs.hilt.android)
  kapt(libs.hilt.android.compiler)

  /**
   * GAM for Storyteller Ads
   */
  implementation(libs.play.services.ads)

  implementation(libs.storyteller) // we need Storyteller SDK :upside_down_face: 🙃
}
