import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version = "1.3.11"
val spek_version = "2.0.0-alpha.2"

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "com.tylerthrailkill.helpers"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://dl.bintray.com/spekframework/spek-dev/")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))

    compile(group= "ch.qos.logback", name= "logback-classic", version= "1.3.0-alpha4")
    compile(group = "org.slf4j", name = "slf4j-simple", version = "1.6.1")
    compile("io.github.microutils:kotlin-logging:1.6.22")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("io.mockk:mockk:1.9.kotlin12")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version") {
        exclude(group = "org.junit.platform")
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("com.beust:klaxon:5.0.1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Wrapper> {
        gradleVersion = "4.10.2"
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
}