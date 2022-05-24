import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.6.10"
//    id("io.qameta.allure") version "2.8.1"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "kz.btsd"
val versionKotest = "5.2.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.kotest:kotest-runner-junit5:$versionKotest")
    implementation(kotlin("stdlib-jdk8"))
}

application {
    mainClassName = "kz.btsd.launcher.MainKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val shadowJar by tasks.getting(ShadowJar::class) {
    isZip64 = true
    manifest {
        attributes["Main-Class"] = "kz.btsd.launcher.MainKt"
    }
    from(sourceSets["main"].output)
}

//allure {
//    autoconfigure = false
//    version = "2.13.1"
//}