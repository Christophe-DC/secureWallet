plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
            api(projects.core.blockchain.wallet)
            api(projects.core.model)
        }
    }
}

android {
    namespace = "com.cdcoding.domain"
}
