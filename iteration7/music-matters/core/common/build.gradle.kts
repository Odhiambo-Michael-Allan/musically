plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.odesa.musicMatters.core.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation( libs.androidx.core.ktx )
    implementation( libs.androidx.appcompat )
    implementation( libs.material )
    implementation( libs.androidx.media )

    // Androidx Media3 Dependencies
    implementation( libs.androidx.media3.common )
    // For exposing and controlling media sessions
    implementation( libs.androidx.media3.session )
    // For media playback using ExoPlayer
    implementation( libs.androidx.media3.exoplayer )

    implementation( libs.kotlinx.coroutines.guava )
    implementation( libs.timber )

    implementation( projects.core.model )
    implementation( projects.core.i8n )
    implementation( projects.core.data )



    testImplementation( libs.junit )
    androidTestImplementation( libs.androidx.junit )
    androidTestImplementation( libs.androidx.espresso.core )
}