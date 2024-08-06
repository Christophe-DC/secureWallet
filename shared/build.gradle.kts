import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("native.cocoapods")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop")


    iosX64()
    iosArm64()
    iosSimulatorArm64()

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
            //export(project(":core:blockchain:wallet"))
            //transitiveExport = true
            //export("dev.icerock.moko:resources:0.22.3")
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

        extraSpecAttributes["resource"] = "'build/cocoapods/framework/shared.framework/*.bundle'"

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
                api(projects.core.network)
                api(projects.core.data)
                api(projects.core.system.ui)
                api(projects.feature.welcome.welcomeDi)
                api(projects.feature.welcome.welcomeImpl)
                api(projects.feature.createwallet.createwalletDi)
                api(projects.feature.createwallet.createwalletImpl)
                api(projects.feature.home.homeDi)
                api(projects.feature.home.homeImpl)
                api(projects.feature.walletdetail.walletdetailDi)
                api(projects.feature.walletdetail.walletdetailImpl)
                //implementation(projects.feature.importWallet.importWalletDi)
                api(libs.koin.compose)
                api(libs.koin.core)
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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

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
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}