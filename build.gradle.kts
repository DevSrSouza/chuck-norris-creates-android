allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/andreyberyukhov/FlowReactiveNetwork")
    }
}

tasks {
    val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}