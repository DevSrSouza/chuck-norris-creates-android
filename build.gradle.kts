allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks {
    val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}