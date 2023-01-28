buildscript {
    repositories.mavenCentral()
}

task<Delete>("clean") {
    delete = setOf(buildDir)
}
