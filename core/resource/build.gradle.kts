plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.securewallet.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.cdcoding.core.resource"
    generateResClass = always
}

android {
    namespace = "com.cdcoding.core.resource"
}
