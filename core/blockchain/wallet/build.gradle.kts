plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
   // kotlin("native.cocoapods")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "wallet"
            isStatic = true
        }
    }


/*    cocoapods {

        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "17"
        //podfile = project.file("../../../iosApp/Podfile")
        //podfile = project.file("src/iosMain/Podfile")
        framework {
            baseName = "wallet"
            isStatic = false
            // Dependency export
            //export(project(":core:blockchain:wallet"))
            //transitiveExport = true
            // Bitcode embedding
            //embedBitcode(BITCODE)
        }
       // pod("TrustWalletCore")
        pod(name = "TrustWalletCore", version = "4.0.49", moduleName = "WalletCore")
        //pod(name = "TrustWalletCore", version = "4.0.49", moduleName = "SwiftProtobuf")
       // pod(name = "TrustWalletCore", url: "https://github.com/trustwallet/wallet-core",  version = "3.1.0", moduleName = "WalletCore")

        /*pod("wallet_wrapper") {
            version = "1.0"
            source = path(project.file("src/iosMain/kotlin/com/cdcoding/wallet/swift"))
            extraOpts += listOf("-compiler-option", "-fmodules")
        }*/



      //  extraSpecAttributes["resource"] = "'build/cocoapods/framework/shared.framework/*.bundle'"
    }*/*/




    sourceSets {
        androidMain.dependencies {
            api(libs.wallet.core)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
           // implementation(libs.compose.runtime.android)
            implementation(projects.core.data.local)
            implementation(projects.core.common)
            implementation(projects.core.model)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }


       /* val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
         //   dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
         //   dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }*/
    }
}

android {
    namespace = "com.cdcoding.wallet"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
