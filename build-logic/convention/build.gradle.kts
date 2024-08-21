plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

group = "com.cdcoding.buildlogic"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


spotless {
    kotlin {
        target("src/**/*.kt")
        ktfmt()
        licenseHeaderFile(rootProject.file("spotless/secure-wallet-copyright.txt")).onlyIfContentMatches("missingString")
    }
    kotlinGradle {
        target("*.kts")
        ktfmt()
        licenseHeaderFile(
            rootProject.file("spotless/secure-wallet-copyright.txt"),
            "(^(?![\\/ ]\\*).*$)",
        ).onlyIfContentMatches("missingString")
    }
    format("xml") {
        target("src/**/*.xml")
        targetExclude("**/build/", ".idea/")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

dependencies {
    compileOnly(libs.spotless)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("coreMultiplatform") {
            id = "securewallet.multiplatform.core"
            implementationClass = "com.cdcoding.convention.KotlinMultiplatformPlugin"
        }
        register("compose") {
            id = "securewallet.compose"
            implementationClass = "com.cdcoding.convention.ComposeMultiplatformPlugin"
        }
    }
}
