plugins {
    id("application")
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<JavaExec> {
    environment("file.encoding", "UTF-8")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.firebirdsql.jdbc:jaybird:5.0.5.java11")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

application {
    mainClass.set("org.example.App")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

tasks.named("distZip") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("distTar") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("startScripts") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("startShadowScripts") {
    dependsOn(tasks.named("shadowJar"))
    dependsOn(tasks.named("jar"))
}
