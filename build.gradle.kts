import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.4.20"
    id("net.fabricmc.fabric-loom") version "1.18.2"
    id("maven-publish")
}

version = getProperty("mod_version")
group = getProperty("maven_group")

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

repositories {
    maven("https://maven.wispforest.io/releases/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:${getProperty("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${getProperty("fabric_loader_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${getProperty("kotlin_loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${getProperty("fabric_api_version")}")
    implementationWithInclude("io.github.classgraph:classgraph:${getProperty("classgraph_version")}")
    implementationWithInclude("net.bytebuddy:byte-buddy:${getProperty("byte_buddy_version")}")
    implementationWithInclude("net.bytebuddy:byte-buddy-agent:${getProperty("byte_buddy_version")}")
}

tasks.processResources {
    inputs.property("mod_name", getProperty("mod_name"))
    inputs.property("version", getProperty("mod_version"))
    inputs.property("project_name", getProperty("project_name"))
    inputs.property("fabric_loader_version", getProperty("fabric_loader_version"))
    inputs.property("kotlin_loader_version", getProperty("kotlin_loader_version"))
    inputs.property("fabric_api_version", getProperty("fabric_api_version"))
    inputs.property("minecraft_version", getProperty("minecraft_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "mod_name" to getProperty("mod_name"),
            "version" to getProperty("mod_version"),
            "project_name" to getProperty("project_name"),
            "fabric_loader_version" to getProperty("fabric_loader_version"),
            "kotlin_loader_version" to getProperty("kotlin_loader_version"),
            "fabric_api_version" to getProperty("fabric_api_version"),
            "minecraft_version" to getProperty("minecraft_version")
        )
    }
}

tasks {
    jar {
        archiveBaseName.set(getProperty("mod_name"))
    }
    registerCopy("buildServer", "D:\\서버\\26.3 - 헌터\\mods")
    registerCopy("buildClient", "C:\\Users\\rhdwl\\AppData\\Roaming\\PrismLauncher\\instances\\26.3 - 헌터\\minecraft\\mods")
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(25)
    }
    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget("25"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = getProperty("mod_name")
            from(components["java"])
        }
    }
}

fun getProperty(path: String): String {
    return property(path) as? String ?: throw IllegalArgumentException()
}

fun TaskContainer.registerCopy(name: String, path: String) {
    register<Copy>(name) {
        group = "1. build"
        dependsOn(jar)
        from(jar)
        into(path)
    }
}

fun DependencyHandlerScope.implementationWithInclude(path: String) {
    implementation(path)
    include(path)
}