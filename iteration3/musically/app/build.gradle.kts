plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.odesa.musically"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.odesa.musically"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val navVersion = "2.7.6"
    val androidxMediaVersion = "1.5.0"
    val androidxMedia3Version =  "1.2.1"
    val kotlinCoroutinesVersion = "1.6.4"

    implementation( "androidx.core:core-ktx:1.12.0" )
    implementation( "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2" )
    implementation( "androidx.activity:activity-compose:1.8.2" )
    implementation( platform( "androidx.compose:compose-bom:2023.08.00" ) )
    implementation( "androidx.compose.ui:ui" )
    implementation( "androidx.compose.ui:ui-graphics" )
    implementation( "androidx.compose.ui:ui-tooling-preview" )
    implementation( "androidx.compose.material3:material3:1.1.2" )
    implementation( "androidx.compose.material:material-icons-extended:1.5.4" )
    implementation( "androidx.navigation:navigation-compose:$navVersion" )
    implementation( "io.coil-kt:coil-compose:2.5.0" )
    implementation( "androidx.media:media:$androidxMediaVersion" )
    implementation( "com.jakewharton.timber:timber:5.0.1" )
    implementation( "com.github.bumptech.glide:glide:4.16.0" )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-guava:$kotlinCoroutinesVersion")

    // Androidx Media3 Dependencies
    implementation( "androidx.media3:media3-common:$androidxMedia3Version" )
    // For exposing and controlling media sessions
    implementation( "androidx.media3:media3-session:$androidxMedia3Version" )
    // For media playback using ExoPlayer
    implementation( "androidx.media3:media3-exoplayer:$androidxMedia3Version" )


    testImplementation( "junit:junit:4.13.2" )
    testImplementation( "androidx.test.ext:junit:1.1.5" )
    testImplementation( "org.robolectric:robolectric:4.11.1" )

    androidTestImplementation( "androidx.test.ext:junit:1.1.5" )
    androidTestImplementation( "androidx.test.espresso:espresso-core:3.5.1" )
    androidTestImplementation( platform( "androidx.compose:compose-bom:2023.08.00" ) )
    androidTestImplementation( "androidx.compose.ui:ui-test-junit4" )
    androidTestImplementation( "androidx.navigation:navigation-testing:$navVersion" )
    testImplementation( "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3" )

    debugImplementation( "androidx.compose.ui:ui-tooling" )
    debugImplementation( "androidx.compose.ui:ui-test-manifest" )
}