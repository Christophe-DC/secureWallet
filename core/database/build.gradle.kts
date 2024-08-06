plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "database"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.sqldelight.runtime)
            api(libs.sqldelight.ext)
            api(libs.sqldelight.adapter)
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)
            api(compose.runtime)
            api(projects.core.common)
            api(projects.core.model)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            api(libs.sqldelight.android)
            api(libs.koin.android)
        }
        iosMain.dependencies {
            api(libs.sqldelight.native)
        }
    }
}

android {
    namespace = "com.cdcoding.database"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("SecureWalletDatabase") {
            packageName.set("com.cdcoding.local.db")
        }
        linkSqlite.set(true)
    }
}
