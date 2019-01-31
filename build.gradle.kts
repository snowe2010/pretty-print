import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version = "1.3.11"
val spek_version = "2.0.0-alpha.2"

plugins {
    `build-scan`
    kotlin("jvm") version "1.3.11"
    id("nebula.maven-publish") version "9.4.6"
    id("nebula.nebula-bintray") version "3.5.2"
    id("nebula.publish-verification") version "9.4.6"
    id("nebula.source-jar") version "9.4.6"
    id("nebula.release") version "9.2.0"
    id("nebula.nebula-bintray-publishing") version "5.0.0"
}

group = "com.tylerthrailkill.helpers"
version = "1.0.0"

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

    compile(group = "ch.qos.logback", name = "logback-classic", version = "1.3.0-alpha4")
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
    testImplementation(group = "org.junit.platform", name = "junit-platform-engine", version = "1.3.0-RC1")
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

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

configure<BintrayExtension> {
    user = findProperty("bintrayUser") as String? ?: System.getenv("BINTRAY_USER")
    key = findProperty("bintrayKey") as String? ?: System.getenv("BINTRAY_KEY")
    override = true

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        userOrg = "snowe"
        repo = "maven"
        name = "Pretty-Print"
        desc = "Pretty printing of objects"

        setLicenses("MIT")
        websiteUrl = "https://github.com/snowe2010/${project.name}"
        issueTrackerUrl = "https://github.com/snowe2010/${project.name}/issues"
        vcsUrl = "https://github.com/snowe2010/${project.name}.git"
        setLabels("axon", "kotlin")
//        version(delegateClosureOf<BintrayExtension.VersionConfig> {
//            gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
//                sign = true
//                passphrase = findProperty("gpgPassphrase") as String? ?: System.getenv("GPG_PASSPHRASE")
//            })
//        })
    })
    dryRun = false
    publish = true
}