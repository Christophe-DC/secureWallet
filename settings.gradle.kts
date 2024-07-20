rootProject.name = "SecureWallet"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":data:local")
include(":core:system:ui")
include(":core:resource")
include(":feature:welcome:welcome-di")
include(":feature:welcome:welcome-impl")
include(":core:navigation")
include(":core:designsystem")
include(":feature:createwallet:createwallet-impl")
include(":feature:createwallet:createwallet-di")
include(":core:blockchain:wallet")
include(":core:domain")
include(":core:data:local")
include(":core:common")
