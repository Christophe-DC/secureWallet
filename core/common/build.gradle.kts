plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.resource)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.lyfecycle.viewmodel)
            implementation(libs.bignum)
        }
    }
}

android {
    namespace = "com.cdcoding.common"
}