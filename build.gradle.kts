import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.qameta.allure") version "2.8.1"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "ru.vladimirsofin"
version = "1.0-SNAPSHOT"
val versionKotest = "5.2.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("io.kotest:kotest-runner-junit5:$versionKotest")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val shadowJar by tasks.getting(ShadowJar::class) {
    isZip64 = true
    manifest {
        attributes["Main-Class"] = "kz.btsd.launcher.main"
    }
    from(sourceSets["test"].output)
}

//allure {
//    autoconfigure = false
//    version = "2.13.1"
//}