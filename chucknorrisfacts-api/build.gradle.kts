plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.ktxSerializationJson)
                implementation(Deps.ktorCore)
                implementation(Deps.ktorSerialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
                implementation(Deps.Test.ktorMock)
                implementation(Deps.Test.coroutines)
                implementation(Deps.Test.mockKUnit)
            }
        }
    }
}
