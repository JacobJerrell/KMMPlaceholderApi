buildscript {
    val kotlin_version = "1.4.20"
    val gradle_version = "4.2.0-beta02"

    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:${gradle_version}")
    }
}


allprojects {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/kotlin/ktor")
        google()
        jcenter()
        mavenCentral()
    }
}