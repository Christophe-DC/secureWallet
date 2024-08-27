plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.securewallet.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.network)
            implementation(projects.core.resource)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kamel.image)
            implementation(libs.chaintech.qrKit)
            implementation(libs.bundles.ktor)
        }
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
    }
}

android {
    namespace = "com.cdcoding.core.designsystem"
}
