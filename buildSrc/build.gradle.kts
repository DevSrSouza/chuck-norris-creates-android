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
    implementation("com.android.tools.build:gradle:4.2.0-alpha16")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.squareup.sqldelight:gradle-plugin:1.4.3")
}