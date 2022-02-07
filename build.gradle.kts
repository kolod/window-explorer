import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val main = "io.github.kolod.WindowExplorer"

version = SimpleDateFormat("yy.M.d").format(Date())

repositories {
    mavenCentral()
	mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
	implementation("org.apache.logging.log4j:log4j-api:2.17.1")
	implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.1")
	implementation("net.java.dev.jna:jna:5.10.0")
	implementation("net.java.dev.jna:jna-platform:5.10.0")
    implementation("com.jcabi:jcabi-manifests:1.1")
    implementation("com.formdev:flatlaf:2.0.1")
}

application {
    mainClass.set(main)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Implementation-Title"] = "WindowExplorer"
        attributes["Main-Class"] = main
        attributes["Author"] = "Oleksandr Kolodkin"
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
        attributes["Multi-Release"] = "true"
        attributes["Build-Date"] = SimpleDateFormat("dd/MM/yyyy").format(Date())
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
