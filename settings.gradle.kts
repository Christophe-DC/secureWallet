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
include(":core:blockchain:wallet")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:navigation")
include(":core:resource")
include(":core:system:ui")
include(":core:model")
include(":core:network")
include(":feature:Amount")
include(":feature:confirm")
include(":feature:createwallet")
include(":feature:home")
include(":feature:SelectAsset")
include(":feature:sendasset")
include(":feature:walletdetail")
include(":feature:welcome")
include(":shared")
include(":feature:receiveasset")
