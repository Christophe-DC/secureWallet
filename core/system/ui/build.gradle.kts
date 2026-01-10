plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.securewallet.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}


android {
    namespace = "com.cdcoding.system.ui"
}
