
import com.jfrog.bintray.gradle.BintrayExtension
import nebula.plugin.contacts.Contact
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val spekVersion = "2.0.3"

plugins {
//    `build-scan`
    kotlin("jvm") version "1.4.0"
    id("nebula.maven-publish") version "9.4.6"
    id("nebula.maven-base-publish") version "9.4.6"
    id("nebula.publish-verification") version "9.4.6"
    id("nebula.source-jar") version "9.4.6"
    id("nebula.javadoc-jar") version "9.4.6"
    id("nebula.info") version "5.0.2"
    id("nebula.release") version "9.2.0"
    id("nebula.nebula-bintray-publishing") version "5.0.0"
    id("nebula.contacts") version "5.0.2"
    id("tylerthrailkill.nebula-mit-license") version "0.0.3"
    id("info.solidsoft.pitest") version "1.5.1"
    jacoco
}

group = "com.tylerthrailkill.helpers"
description = "Pretty printing of objects"

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/spekframework/spek-dev/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
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

    testImplementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.4.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("io.mockk:mockk:1.9.kotlin12")
    testImplementation("io.kotest:kotest-runner-junit5:4.3.0.621-SNAPSHOT") // for kotest framework
    testImplementation("com.beust:klaxon:5.0.1") // used to parse naughty list
    testImplementation(group = "org.junit.platform", name = "junit-platform-engine", version = "1.3.0-RC1")
    testImplementation("io.kotest:kotest-plugins-pitest:4.3.0.621-SNAPSHOT")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Wrapper> {
        distributionType = Wrapper.DistributionType.ALL
    }

    withType<Test> {
        useJUnitPlatform {}
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
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

sourceSets.test {
    java.srcDirs("src/test/kotlin")
}

//buildScan {
//    termsOfServiceUrl = "https://gradle.com/terms-of-service"
//    termsOfServiceAgree = "yes"
//}

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
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
                sign = true
                passphrase = findProperty("gpgPassphrase") as String? ?: System.getenv("GPG_PASSPHRASE")
            })
            mavenCentralSync(delegateClosureOf<BintrayExtension.MavenCentralSyncConfig> {
                user = findProperty("sonatypeUser") as String? ?: System.getenv("SONATYPE_USERNAME") //OSS user token: mandatory
                password = findProperty("sonatypePassword") as String? ?: System.getenv("SONATYPE_PASSWORD") //OSS user password: mandatory
            })
        })
    })
    dryRun = false
    publish = true
}

contacts {
    // Use statically-typed extension accessor
    addPerson("tyler.b.thrailkill@gmail.com", delegateClosureOf<Contact> {
        // type-safe adapter for dynamic Groovy Closure
        moniker = "Tyler Thrailkill" // This can be assigned as property
        role("owner") // To add role to `roles` set, you also can call it directly `roles.add("owner")`
    })
}

//pitest {
//    junit5PluginVersion = "0.12"
//    testPlugin = "junit5"
//    pitestVersion = "1.5.1"
//    targetClasses = setOf("com.tylerthrailkill.*")
//    targetTests = setOf("com.tylerthrailkill.**.*")
//    verbose = true
//    mainProcessJvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")
//    jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")
//    jvmArgs = listOf("-Xdebug","-Xnoagent", "-Djava.compiler=NONE", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006")
//}

configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
    testPlugin.set("Kotest")    // <-- Telling Pitest that we're using Kotest
    targetClasses.set(listOf("com.tylerthrailkill.*"))
}
