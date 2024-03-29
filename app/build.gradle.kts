plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")


}

android {
    namespace = "com.example.fitmaptracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fitmaptracker"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildToolsVersion = "33.0.1"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Material Design
    implementation ("com.google.android.material:material:1.12.0-alpha03")

    // Architectural Components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")


    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.1")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coroutine Lifecycle Scopes
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Navigation Components
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // Google Maps Location Services
    implementation ("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")


    // Dagger Core
    implementation ("com.google.dagger:dagger:2.50")
    kapt ("com.google.dagger:dagger-compiler:2.50")

    // Dagger Android
    api ("com.google.dagger:dagger-android:2.50")
    api ("com.google.dagger:dagger-android-support:2.50")
    kapt ("com.google.dagger:dagger-android-processor:2.50")


    // Easy Permissions
    implementation ("pub.devrel:easypermissions:3.0.0")

    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    implementation ("androidx.lifecycle:lifecycle-runtime:2.7.0")





}