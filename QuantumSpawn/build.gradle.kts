repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":Quantum"))

    implementation("org.spongepowered:configurate-hocon:4.0.0")

    implementation("net.kyori:adventure-api:4.3.0")
    implementation("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    api("cloud.commandframework:cloud-paper:1.3.0-SNAPSHOT")
}