plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.core.common)
            implementation(projects.core.model)
        }

        androidMain.dependencies {
            implementation(libs.walletconnect.core)
            implementation(libs.walletconnect.sign)
        }
    }
}

android {
    namespace = "com.cdcoding.datasource"
}
