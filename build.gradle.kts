import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version("6.1.0")
    id("maven-publish")
}

group = "dev.kscott"
version = "1.0-SNAPSHOT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = sourceCompatibility
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnlyApi("org.checkerframework:checker-qual:3.5.0")
    compileOnlyApi("com.google.guava:guava:21.0")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")

    implementation("com.github.stefvanschie.inventoryframework:IF:0.9.0")

    implementation("org.spongepowered:configurate-hocon:4.0.0")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    api("com.google.inject:guice:4.2.3")
    api("cloud.commandframework:cloud-paper:1.2.0")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split('.')
                val name = split.last();
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
        }

        relocates(
                "com.github.benmanes.caffeine",
                "com.typesafe.config",
                "com.zaxxer.hikari",
                "com.google.inject",
                "org.antlr",
                "org.slf4j",
                "org.jdbi",
                "org.aopalliance",
                "org.spongepowered.configurate",
                "io.leangen.geantyref",
                "cloud.commandframework",
                "net.kyori.adventure",
                "net.kyori.examination",
                "com.github.stefvanschie.inventoryframework"
        )

        archiveFileName.set(project.name + ".jar")
        minimize()
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}