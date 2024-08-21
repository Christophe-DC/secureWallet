plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.securewallet.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.domain)
            implementation(projects.core.navigation)
            implementation(projects.core.resource)
            implementation(projects.core.system.ui)
            implementation(libs.voyager)
            implementation(libs.voyager.tabNavigator)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.chaintech.qrKit)
        }
    }
}

android {
    namespace = "com.cdcoding.receiveasset"
}
