plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    kotlin("native.cocoapods")
}

kotlin {
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"

        noPodspec()
        dependencies {
            pod("TrustWalletCore", moduleName = "WalletCore")
        }
        podfile = project.file("../../../iosApp/Podfile")
        framework {
            baseName = "wallet"
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
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.serialization.json)
                api(libs.wallet.core)
                api(projects.core.database)
                api(projects.core.common)
                api(projects.core.datastore)
                api(projects.core.model)
                api(libs.bignum)
            }
        }

        val commonTest by getting
        val androidMain by getting

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
    namespace = "com.cdcoding.wallet"
}
