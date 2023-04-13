package com.gulderbone

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

internal fun Project.configureDetekt() {
    extensions.configure<DetektExtension> {
        config = files("$rootDir/config/detekt/detekt.yml")
    }
}
