repositories {
    mavenLocal()

    maven {
        name = "EssentialsX"
        url = uri("https://repo.essentialsx.net/snapshots/")
    }
}

dependencies {
    compileOnly(projects.quantumAPI)

    compileOnly("org.spongepowered:configurate-hocon:4.0.0")

    compileOnly("net.kyori:adventure-api:4.3.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")

    compileOnly("cloud.commandframework:cloud-paper:1.4.0")
    implementation("cloud.commandframework:cloud-minecraft-extras:1.4.0") {
        isTransitive = false
    }

    compileOnly("net.luckperms:api:5.2")

    compileOnly("net.essentialsx:EssentialsX:2.19.0-SNAPSHOT")
}