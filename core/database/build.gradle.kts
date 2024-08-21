plugins {
    alias(libs.plugins.securewallet.multiplatform.core)
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.sqldelight.runtime)
            api(libs.sqldelight.ext)
            api(libs.sqldelight.adapter)
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)
            api(projects.core.common)
            api(projects.core.model)
        }
        androidMain.dependencies {
            api(libs.sqldelight.android)
            api(libs.koin.android)
        }
        iosMain.dependencies {
            api(libs.sqldelight.native)
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.sqldelight.desktop)
        }
    }
}

android {
    namespace = "com.cdcoding.database"
}

sqldelight {
    databases {
        create("SecureWalletDatabase") {
            packageName.set("com.cdcoding.local.db")
        }
        linkSqlite.set(true)
    }
}
