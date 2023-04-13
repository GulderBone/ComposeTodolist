package com.gulderbone

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider


fun Project.getVersionCatalog(): VersionCatalog =
    extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getVersion(name: String): String =
    findVersion(name).get().toString()

fun VersionCatalog.getLib(name: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(name).get()
