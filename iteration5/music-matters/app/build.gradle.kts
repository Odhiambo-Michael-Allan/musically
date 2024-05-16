plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.odesa.musicMatters"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.odesa.musicMatters"
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

    implementation( libs.androidx.core.ktx )
    implementation( libs.androidx.lifecycle.runtime.ktx )

    implementation( libs.androidx.compose.runtime )
    implementation( libs.androidx.activity.compose )
    implementation( platform( libs.androidx.compose.bom ) )
    implementation( libs.androidx.compose.ui )
    implementation( libs.androidx.compose.animation )
    implementation( libs.androidx.ui.graphics )


    implementation( libs.androidx.ui.tooling.preview )
    implementation( libs.androidx.material3 )
    implementation( libs.compose.material.icons.extended )
    implementation( libs.compose.navigation )
    implementation( libs.coil )
    implementation( "androidx.media:media:$androidxMediaVersion" )
    implementation( "com.jakewharton.timber:timber:5.0.1" )
    implementation( "com.github.bumptech.glide:glide:4.16.0" )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-guava:$kotlinCoroutinesVersion" )

    // Androidx Media3 Dependencies
    implementation( "androidx.media3:media3-common:$androidxMedia3Version" )
    // For exposing and controlling media sessions
    implementation( "androidx.media3:media3-session:$androidxMedia3Version" )
    // For media playback using ExoPlayer
    implementation( "androidx.media3:media3-exoplayer:$androidxMedia3Version" )

    implementation( "sh.calvin.reorderable:reorderable:1.5.0" )


    testImplementation( libs.junit )
    testImplementation( "androidx.test.ext:junit:1.1.5" )
    testImplementation( libs.robolectric )

    androidTestImplementation( libs.androidx.junit )
    androidTestImplementation( libs.androidx.espresso.core )
    androidTestImplementation( platform( libs.androidx.compose.bom ) )
    androidTestImplementation( libs.androidx.ui.test.junit4 )
    androidTestImplementation( libs.androidx.navigation.testing )
    testImplementation( libs.kotlinx.coroutines.test )

    debugImplementation( libs.androidx.ui.tooling )
    debugImplementation( libs.androidx.ui.test.manifest )
}