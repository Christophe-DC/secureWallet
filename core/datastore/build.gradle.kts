plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.model)
            api(libs.androidx.datastore)
            api(libs.androidx.datastore.preferences)
        }

        androidMain.dependencies {
            // EncryptedPreferences
            implementation(libs.androidx.security.crypto)
        }
    }
}

android {
    namespace = "com.cdcoding.datastore"
}
