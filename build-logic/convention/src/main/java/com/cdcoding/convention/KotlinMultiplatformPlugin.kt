package com.cdcoding.convention

import com.android.build.api.dsl.LibraryExtension
import com.cdcoding.convention.multiplatform.configureCoreMultiplatform
import com.cdcoding.convention.multiplatform.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.multiplatform")
        }
        configureSpotless()
        extensions.configure<KotlinMultiplatformExtension> {
            configureCoreMultiplatform(this)
        }
        extensions.configure<LibraryExtension>(::configureKotlinAndroid)
    }
}


