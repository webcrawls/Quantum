repositories {
    mavenLocal()

    maven {
        name = "EssentialsX"
        url = uri("https://repo.essentialsx.net/snapshots/")
    }

    maven {
        name = "bStats"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
}

dependencies {
    compileOnly(project(":QuantumAPI"))

    compileOnly("org.spongepowered:configurate-hocon:4.0.0")

    compileOnly("net.kyori:adventure-api:4.3.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    compileOnly("cloud.commandframework:cloud-paper:1.3.0-SNAPSHOT")

    compileOnly("net.luckperms:api:5.2")

    compileOnly("net.essentialsx:EssentialsX:2.19.0-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:1.8")
}