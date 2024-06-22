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
    implementation( libs.androidx.media )
    implementation( libs.timber )
    implementation( libs.kotlinx.coroutines.guava )

    // Androidx Media3 Dependencies
    implementation( libs.androidx.media3.common )
    // For exposing and controlling media sessions
    implementation( libs.androidx.media3.session )
    // For media playback using ExoPlayer
    implementation( libs.androidx.media3.exoplayer )

    implementation( "sh.calvin.reorderable:reorderable:1.5.0" )

    implementation( projects.core.designsystem )
    implementation( projects.core.common )
    implementation( projects.core.data )
    implementation( projects.core.datatesting )
    implementation( projects.core.i8n )
    implementation( projects.core.model )

    testImplementation( libs.junit )
    testImplementation( libs.androidx.junit )
    testImplementation( libs.robolectric )
    testImplementation( libs.kotlinx.coroutines.test )

    androidTestImplementation( libs.androidx.junit )
    androidTestImplementation( libs.androidx.espresso.core )
    androidTestImplementation( platform( libs.androidx.compose.bom ) )
    androidTestImplementation( libs.androidx.ui.test.junit4 )
    androidTestImplementation( libs.androidx.navigation.testing )


    debugImplementation( libs.androidx.ui.tooling )
    debugImplementation( libs.androidx.ui.test.manifest )
}