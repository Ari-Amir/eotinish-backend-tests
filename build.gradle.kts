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
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.kotest:kotest-runner-junit5:$versionKotest")

    implementation("org.json:json:20180813")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("org.testcontainers:testcontainers:1.11.2")
    implementation("org.testcontainers:postgresql:1.12.3")
    implementation("org.postgresql:postgresql:42.2.7")
    implementation("org.springframework:spring-jdbc:5.1.9.RELEASE")
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