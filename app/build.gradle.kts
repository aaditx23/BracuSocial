import kotlin.script.experimental.jvm.util.classpathFromClass

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("io.realm.kotlin")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")

    //firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.aaditx23.bracusocial"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aaditx23.bracusocial"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "alpha_01"

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
            signingConfig = signingConfigs.getByName("debug")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("org.jsoup:jsoup:1.15.4")
    implementation("org.json:json:20230227")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.fleeksoft.ksoup:ksoup:0.2.0")

    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.webkit:webkit:1.12.1") // for WebView in Compose


    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")




    implementation("androidx.compose.material:material-icons-extended:1.6.5")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.realm.kotlin:library-base:1.16.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore-ktx")



    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries







    ksp("com.google.dagger:hilt-compiler:2.49")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
}