import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val githubUsername = "snowe2010"
val repoName = "pretty-print"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}

plugins {
    `maven-publish`
    `java-library`
    jacoco
    id("com.jfrog.bintray") version "1.8.5"
    kotlin("jvm") version "1.4.10"
    id("info.solidsoft.pitest") version "1.5.1"
}

group = "com.tylerthrailkill.helpers"
description = "Pretty printing of objects"
version = Ci.publishVersion

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
    testImplementation("com.beust:klaxon:5.0.1") // used to parse naughty list
    testImplementation(group = "org.junit.platform", name = "junit-platform-engine", version = "1.3.0-RC1")

    testImplementation("io.kotest:kotest-runner-junit5:4.3.0.621-SNAPSHOT") // for kotest framework
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

configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
    testPlugin.set("Kotest")    // <-- Telling Pitest that we're using Kotest
    targetClasses.set(listOf("com.tylerthrailkill.*"))
//    avoidCallsTo.set(listOf("java.util.logging", "org.apache.log4j", "org.slf4j", "org.apache.commons.logging", "mu"))
}


/**
 * Publishing
 */
configure<BintrayExtension> {
    user = findProperty("bintrayUser") as String? ?: System.getenv("BINTRAY_USER")
    key = findProperty("bintrayKey") as String? ?: System.getenv("BINTRAY_API_KEY")
    override = true

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        userOrg = "snowe"
        repo = "maven"
        name = "Pretty-Print"
        desc = "Pretty printing of objects"

        setLicenses("MIT")
        websiteUrl = "https://github.com/$githubUsername/${project.name}"
        issueTrackerUrl = "https://github.com/$githubUsername/${project.name}/issues"
        vcsUrl = "https://github.com/$githubUsername/${project.name}.git"
        setLabels("kotlin")
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            this.name = Ci.publishVersion
            gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
                sign = true
                passphrase = findProperty("gpgPassphrase") as String? ?: System.getenv("GPG_PASSPHRASE")
            })
            mavenCentralSync(delegateClosureOf<BintrayExtension.MavenCentralSyncConfig> {
                user = findProperty("sonatypeUser") as String?
                    ?: System.getenv("SONATYPE_USERNAME") //OSS user token: mandatory
                password = findProperty("sonatypePassword") as String?
                    ?: System.getenv("SONATYPE_PASSWORD") //OSS user password: mandatory
            })
        })
    })
    dryRun = false
    publish = true
}

val javadoc = tasks.named("javadoc")
val javadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles java doc to jar"
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubUsername/$repoName")
            credentials {
                username = System.getenv("GITHUB_USERNAME") ?: project.findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }

    publications.withType<MavenPublication>().forEach {
        it.apply {
            artifact(javadocJar)
            pom {
                name.set(repoName)
                description.set("Pretty printing of objects")
                url.set("http://www.github.com/$githubUsername/$repoName")

                scm {
                    connection.set("scm:git:http://www.github.com/$githubUsername/$repoName")
                    developerConnection.set("scm:git:http://github.com/$githubUsername/")
                    url.set("http://www.github.com/snowe2010/$repoName/")
                }

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("snowe")
                        name.set("Tyler Thrailkill")
                        email.set("tyler.b.thrailkill@gmail.com")
                    }
                }
            }
        }
    }
}
