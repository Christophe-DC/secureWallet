plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.bundles.ktor)
            implementation(libs.bignum)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.cdcoding.model"
}
