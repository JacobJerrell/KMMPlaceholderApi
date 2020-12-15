plugins {
    id("com.android.application")
    kotlin("android")
}

val lifecycle_version = "2.2.0"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    implementation("androidx.fragment:fragment-ktx:1.2.5")

    /**
     * Java 8 desugaring exposes (a subset of) Java 8+ APIs without raising the minimum Android API level
     * Reference: https://developer.android.com/studio/write/java8-support#library-desugaring
     * Exposes: https://developer.android.com/studio/write/java8-support-table
     */
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.bocaj.kmmplaceholder.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        // https://stackoverflow.com/a/64064557
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}