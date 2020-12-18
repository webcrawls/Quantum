repositories {
    mavenLocal()
}

dependencies {
    compileOnly(project(":QuantumAPI"))

    compileOnly("org.spongepowered:configurate-hocon:4.0.0")

    compileOnly("net.kyori:adventure-api:4.3.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    compileOnly("cloud.commandframework:cloud-paper:1.3.0-SNAPSHOT")

}