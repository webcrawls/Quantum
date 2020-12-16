import org.apache.tools.ant.filters.ReplaceTokens
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.checkerframework.gradle.plugin.CheckerFrameworkPlugin


plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version("6.1.0")
    id("org.checkerframework") version("0.5.13")
}

allprojects {
    group = "dev.kscott.quantum"
    version = "1.0-SNAPSHOT"
}

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<ShadowPlugin>()
        plugin<CheckerFrameworkPlugin>()
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

        implementation("net.kyori:adventure-api:4.3.0")
        implementation("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
        implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

        api("com.google.inject:guice:4.2.3")
        api("cloud.commandframework:cloud-paper:1.2.0")
    }

    tasks {
        withType<ShadowJar> {

            // Make this the default jar
            this.archiveClassifier.set(null as String?)

            this.archiveBaseName.set("${rootProject.name}-${project.name}")

            if (System.getenv("DISABLE_VERSION_JAR") != null) {
                // The user does not want us to have a version attached to the jar name.
                this.archiveVersion.set(null as String?)
            }

            dependencies {
                exclude(dependency("org.checkerframework:checker-qual"))
            }


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
                    "com.google.inject",
                    "org.antlr",
                    "org.slf4j",
                    "org.spongepowered.configurate",
                    "cloud.commandframework",
                    "net.kyori.adventure",
                    "net.kyori.examination",
                    "com.github.stefvanschie.inventoryframework"
            )

            archiveFileName.set(project.name + ".jar")
            minimize()
        }
        build {
            dependsOn(shadowJar)
        }

        processResources {
            filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = sourceCompatibility
    }
}