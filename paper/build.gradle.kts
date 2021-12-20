dependencies {
    implementation(project(":quantum-core"))
    implementation(libs.cloud.paper)
    compileOnly(libs.paper.api)
}

tasks {
    build {
        dependsOn(named("shadowJar"))
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split(".")
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
        }

        relocates(
                "org.spongepowered.configurate"
        )

        archiveFileName.set("${project.name}-${rootProject.version}.jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }

    processResources {
        expand("version" to project.version)
    }
}