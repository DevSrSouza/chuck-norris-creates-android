allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}