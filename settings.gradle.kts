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
include(":core:common")
include(":core:model")
include(":feature:home:home-impl")
include(":feature:home:home-di")
include(":feature:walletdetail:walletdetail-di")
include(":feature:walletdetail:walletdetail-impl")
include(":core:network")
include(":core:datastore")
include(":core:database")
include(":core:data")
include(":shared")
include(":feature:SelectAsset")
include(":feature:sendasset")
include(":feature:Amount")
include(":feature:confirm")
