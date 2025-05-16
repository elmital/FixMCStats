plugins {
    `java-library`
    `maven-publish`
    idea
    id("net.neoforged.moddev") version "2.0.88"
}

version = project.property("mod_version") as String
group = project.property("mod_group_id") as String

repositories {
    mavenLocal()
}

base {
    archivesName = project.property("mod_id") as String
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

neoForge {
    version = project.property("neo_version") as String

    parchment {
        mappingsVersion = project.property("parchment_mappings_version") as String
        minecraftVersion = project.property("parchment_minecraft_version") as String
    }

    accessTransformers.from(project.file("src/main/resources/META-INF/accesstransformer.cfg").absolutePath)

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("data") {
            clientData()

            programArguments.addAll(
                "--mod", project.property("mod_id") as String,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")

            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(project.property("mod_id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

dependencies {
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to project.property("minecraft_version") as String,
        "minecraft_version_range" to project.property("minecraft_version_range") as String,
        "neo_version" to project.property("neo_version") as String,
        "neo_version_range" to project.property("neo_version_range") as String,
        "loader_version_range" to project.property("loader_version_range") as String,
        "mod_id" to project.property("mod_id") as String,
        "mod_name" to project.property("mod_name") as String,
        "mod_license" to project.property("mod_license") as String,
        "mod_version" to project.property("mod_version") as String,
        "mod_authors" to project.property("mod_authors") as String,
        "mod_description" to project.property("mod_description") as String
    )
    inputs.properties(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
    expand(replaceProperties)
}

sourceSets.main {
    resources.srcDir(generateModMetadata)
}
neoForge.ideSyncTask(generateModMetadata)

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}