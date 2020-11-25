plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":chucknorrisfacts-api"))
                implementation(Deps.coroutinesCore)
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
