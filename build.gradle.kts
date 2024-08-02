plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    repositories {
        val propFile = File(rootDir.absolutePath, "local.properties")
        var properties = java.util.Properties()
        if (propFile.exists()) {
            properties = properties.apply {
                propFile.inputStream().use { fis ->
                    load(fis)
                }
            }
        }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")
            credentials {
                username = properties["gpr.user"] as? String ?: System.getenv("GRP_USERNAME")
                password = properties["gpr.key"] as? String ?: System.getenv("GRP_TOKEN")
            }
        }
    }
}
