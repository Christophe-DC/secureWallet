import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.securewallet.compose)
    kotlin("native.cocoapods")
}

kotlin {
   /* listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = false
        }
    }*/


    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"

        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "shared"
            linkerOpts.add("-lsqlite3")
            export(projects.core.resource)
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            ).forEach {
                it.binaries.all {
                    linkerOpts += "-ld64"
                }
            }
        }

        dependencies {
            pod("TrustWalletCore", moduleName = "WalletCore")
        }

       // extraSpecAttributes["resource"] = "'build/cocoapods/framework/shared.framework/*.bundle'"

        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.blockchain.wallet)
                api(projects.core.datastore)
                api(projects.core.domain)
                api(projects.core.data)
                api(projects.core.database)
                api(projects.core.navigation)
                api(projects.core.network)
                api(projects.core.data)
                api(projects.core.designsystem)
                api(projects.core.resource)
                api(projects.core.system.ui)
                api(projects.feature.welcome)
                api(projects.feature.createwallet)
                api(projects.feature.amount)
                api(projects.feature.confirm)
                api(projects.feature.home)
                api(projects.feature.importwallet)
                api(projects.feature.receiveasset)
                api(projects.feature.selectAsset)
                api(projects.feature.selectwallet)
                api(projects.feature.sendasset)
                api(projects.feature.walletdetail)
                //implementation(projects.feature.importWallet.importWalletDi)
                api(libs.koin.compose)
                api(libs.navigation.compose)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.ui)
                api(compose.components.resources)
                api(compose.components.uiToolingPreview)
                api(libs.voyager)
                implementation(libs.voyager.transitions)
            }
        }
        val commonTest by getting

        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.cdcoding.shared"
}