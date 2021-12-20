import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin

plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.0.0")
    id("org.checkerframework")
}

group = "sh.kaden.quantum"
version = "2.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<ShadowPlugin>()
        plugin<org.checkerframework.gradle.plugin.CheckerFrameworkPlugin>();
    }

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    tasks {
        processResources {
            expand("version" to rootProject.version)
        }
    }
}