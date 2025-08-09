plugins {
    id("java")
    kotlin("jvm") version "1.9.22" // Especificá la versión de Kotlin
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Implementación estándar de la biblioteca de Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Dependencias para pruebas con JUnit 5
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

// Configuración de las fuentes para Kotlin y Java
sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
    test {
        java.srcDirs("src/test/kotlin")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(18)
}