object Deps {
    // ktor
    private const val ktorVersion = "1.4.2"

    val ktorCore = "io.ktor:ktor-client-core:$ktorVersion"
    val ktorAndroid = "io.ktor:ktor-client-android:$ktorVersion"
    val ktorIOS = "io.ktor:ktor-client-ios:$ktorVersion"
    val ktorSerialization = "io.ktor:ktor-client-serialization:$ktorVersion"

    // coroutines
    private const val coroutinesVersion = "1.4.1"

    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    // kotlinx.serialization
    private const val ktxSerializationVersion = "1.0.1"

    val ktxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:$ktxSerializationVersion"

    // SQLDelight
    private const val sqlDelightVersion = "1.4.3"

    val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:$sqlDelightVersion"
    val sqlDelightNative = "com.squareup.sqldelight:native-driver:$sqlDelightVersion"

    // material design
    private const val materialDesignVersion = "1.3.0-alpha03"

    val materialDesign = "com.google.android.material:material:$materialDesignVersion"

    object AndroidX {
        private const val coreVersion = "1.3.2"
        val core = "androidx.core:core-ktx:$coreVersion"

        private const val appCompatVersion = "1.2.0"
        val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
        val appCompatResources = "androidx.appcompat:appcompat-resources:$appCompatVersion"

        private const val fragmentVersion = "1.2.5"
        val fragment = "androidx.fragment:fragment-ktx:$fragmentVersion"

        private const val lifecycleVersion = "2.2.0"
        val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"

        private const val navigationVersion = "2.3.1"
        val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:$navigationVersion"
        val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
        val navigationUi = "androidx.navigation:navigation-ui-ktx:$navigationVersion"

        private const val recyclerViewVersion = "1.0.0"
        val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"

        private const val constraintLayoutVersion = "2.0.4"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    }

    object Test {
        val junit = "junit:junit:4.12"
        val junitAndroidX = "androidx.test.ext:junit:1.1.1"
        val fragmentTesting = "androidx.fragment:fragment-testing:1.2.5"
        val expresso = "androidx.test.espresso:espresso-core:3.2.0-beta01"
        val expressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:3.2.0-beta01"
        val expressoContrib = "androidx.test.espresso:espresso-contrib:3.2.0-beta01"
        val testRunner = "androidx.test:runner:1.1.0"
        val testRules = "androidx.test:rules:1.1.0"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"

        val ktorMock = "io.ktor:ktor-client-mock:$ktorVersion"

        private const val mockKVersion = "1.10.2"
        val mockKCommon = "io.mockk:mockk-common:$mockKVersion"
        val mockKUnit = "io.mockk:mockk:$mockKVersion"
        val mockKAndroid = "io.mockk:mockk-android:$mockKVersion"
    }
}