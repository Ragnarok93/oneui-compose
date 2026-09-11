@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "org.oneui.compose"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform(libs.compose.bom))

    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowsizeclas)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.animation)
    implementation(libs.io.github.oneuiproject.icons)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.jetbrains.kotlinx.coroutines.core)

    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    coreLibraryDesugaring(libs.coreLibDesugaring)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.Ragnarok93"
                artifactId = "oneui-compose"
                version = "0.8.0"
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}
