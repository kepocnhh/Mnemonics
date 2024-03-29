object Version {
    const val jvmTarget = "11"
    const val kotlin = "1.7.10"

    object Android {
        const val compileSdk = 33
        const val minSdk = 24
        const val targetSdk = compileSdk
        const val compose = "1.3.1" // require kotlin 1.7.10
    }

    object Application {
        const val code = 6
        const val name = "0.0.$code"
    }
}
