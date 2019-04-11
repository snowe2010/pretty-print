import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val spekVersion = "2.0.0-alpha.2"

buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("com.tylerthrailkill.gradle:nebula-mit-license:0.0.1")
    }
}
plugins {
    `build-scan`
    kotlin("jvm") version "1.3.11"
    id("nebula.maven-publish") version "9.4.6"
    id("nebula.maven-base-publish") version "9.4.6"
    id("nebula.publish-verification") version "9.4.6"
    id("nebula.source-jar") version "9.4.6"
    id("nebula.javadoc-jar") version "9.4.6"
    id("nebula.info") version "5.0.2"
    id("nebula.release") version "9.2.0"
    id("nebula.nebula-bintray-publishing") version "5.0.0"
//    id("tylerthrailkill.nebula-mit-license") version "0.0.1"
    jacoco
}
apply(plugin = "tylerthrailkill.nebula-mit-license")
group = "com.tylerthrailkill.helpers"
description = "Pretty printing of objects"

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven(url = "https://dl.bintray.com/spekframework/spek-dev/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    implementation("com.ibm.icu:icu4j:63.1")

    // logging
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.3.0-alpha4")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.6.1")
    implementation("io.github.microutils:kotlin-logging:1.6.22")

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("io.mockk:mockk:1.9.kotlin12")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion") {
        exclude(group = "org.junit.platform")
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("com.beust:klaxon:5.0.1") // used to parse naughty list
    testImplementation(group = "org.junit.platform", name = "junit-platform-engine", version = "1.3.0-RC1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Wrapper> {
        distributionType = Wrapper.DistributionType.ALL
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
    val report = withType<JacocoReport> {
        reports {
            xml.apply {
                isEnabled = true
                this.destination = file("$buildDir/jacoco/report.xml")
            }
            html.apply {
                isEnabled = true
                this.destination = file("$buildDir/jacoco/html_report")
            }
        }
        dependsOn("test")
    }
    this["check"].dependsOn(report)
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
