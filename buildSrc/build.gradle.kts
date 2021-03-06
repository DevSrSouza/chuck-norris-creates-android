plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

val kotlinVersion = "1.4.10"

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:4.2.0-beta01")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
}