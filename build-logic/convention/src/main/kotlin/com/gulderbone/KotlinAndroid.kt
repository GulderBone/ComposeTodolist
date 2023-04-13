package com.gulderbone

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    val versionCatalog = getVersionCatalog()

    commonExtension.apply {
        compileSdk = versionCatalog.getVersion("compileSdk").toInt()

        defaultConfig {
            minSdk = versionCatalog.getVersion("minSdk").toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )

            jvmTarget = "11"
        }

        kotlin {
            jvmToolchain(11)
        }

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            animationsDisabled = true
        }

        sourceSets.all { sourceSet ->
            sourceSet.java.srcDir("src/${sourceSet.name}/kotlin")
        }

        tasks.withType<Test>().configureEach {
            it.useJUnitPlatform()
        }
    }
}

private fun Project.kotlin(block: KotlinAndroidProjectExtension.() -> Unit) {
    block(extensions.getByType())
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
