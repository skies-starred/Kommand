plugins {
    kotlin("jvm") version "2.4.10"
    `maven-publish`
}

val projectName = "kommand"
val projectGroup = "foo.starred"
val projectVersion = "2026.09.1"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation("com.mojang:brigadier:1.3.10")
}

publishing {
    repositories {
        val a = if (Regex("-b[0-9]*$") in projectVersion) "snapshots" else "releases"
        maven("https://maven.starred.foo/$a") {
            name = "starred"
            credentials {
                username = (project.findProperty("MAVEN_USER") as? String) ?: System.getenv("MAVEN_USER") ?: ""
                password = (project.findProperty("MAVEN_PASS") as? String) ?: System.getenv("MAVEN_PASS") ?: ""
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = projectGroup
            artifactId = projectName
            version = projectVersion
            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(21)
}
