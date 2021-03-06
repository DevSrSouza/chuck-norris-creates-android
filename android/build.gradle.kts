plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "br.com.devsrsouza.chucknorrisfacts"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = ProjectVersions.versionCode
        versionName = ProjectVersions.versionName

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("DebugProbesKt.bin")
    }
}

dependencies {
    implementation(project(":repository"))

    implementation(kotlin("stdlib"))
    implementation(Deps.coroutinesAndroid)

    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.lifecycleLiveData)
    implementation(Deps.AndroidX.lifecycleExtensions)

    implementation(Deps.materialDesign)

    implementation(Deps.ktorAndroid)

    // Testing
    testImplementation(kotlin("test-junit"))
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.coroutines)
    testImplementation(Deps.Test.mockKUnit)
    testImplementation(Deps.Test.flowTestObserver)
    testImplementation(Deps.Test.coreTesting)

    androidTestImplementation(Deps.Test.junitAndroidX)
    androidTestImplementation(Deps.Test.fragmentTesting)
    androidTestImplementation(Deps.Test.testRunner)
    androidTestImplementation(Deps.Test.testRules)
    androidTestImplementation(Deps.Test.mockKAndroid)

    androidTestImplementation(Deps.Test.expresso)
    androidTestImplementation(Deps.Test.expressoContrib)
    implementation(Deps.Test.expressoIdlingResource)
}