rootProject.name = "quantum"

listOf("core", "paper").forEach {
    include(it)
    project(":$it").name = "quantum-$it"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.stellardrift.ca/repository/snapshots/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0-SNAPSHOT"
}