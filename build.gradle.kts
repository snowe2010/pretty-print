import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val githubUsername = "snowe2010"
val repoName = "pretty-print"

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}

plugins {
    `maven-publish`
    `java-library`
    jacoco
    kotlin("jvm") version "2.2.0"
    id("info.solidsoft.pitest") version "1.19.0-rc.3"
    id("com.arcmutate.github") version "2.3.2"
}

println("+++++++${System.getenv("RELEASE_VERSION")}++++++")

group = "com.tylerthrailkill.helpers"
description = "Pretty printing of objects"
version = Ci.publishVersion

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    implementation("com.ibm.icu:icu4j:63.2")

    // logging
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    // Test

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.beust:klaxon:5.5") // used to parse naughty list
    testImplementation("org.junit.platform:junit-platform-engine:1.8.1")

    testImplementation("io.kotest:kotest-runner-junit5:6.1.6") // for kotest framework
    pitest("io.kotest:kotest-extensions-pitest:6.1.6")
    pitest("com.arcmutate:base:1.7.0")
    pitest("com.arcmutate:pitest-kotlin-plugin:1.5.0")
    pitest("com.arcmutate:pitest-accelerator-junit5:1.2.2")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

tasks {
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
                required.set(true)
                outputLocation.set(file("${layout.buildDirectory.get()}/jacoco/report.xml"))
            }
            html.apply {
                required.set(true)
                outputLocation.set(file("${layout.buildDirectory.get()}/jacoco/html_report"))
            }
        }
        dependsOn("test")
    }
    this["check"].dependsOn(report)
}

sourceSets.test {
    java.srcDirs("src/test/kotlin")
}

 pitest {
    targetClasses.set(listOf("com.tylerthrailkill.*"))
    pitestVersion = "1.22.1"
    features = listOf("+KOTLIN_NO_NULLS", "+auto_threads")
    mutators.addAll("STRONGER", "EXTENDED", "KOTLIN_RETURNS", "KOTLIN_REMOVE_DISTINCT", "KOTLIN_REMOVE_SORTED")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

jacoco {
    toolVersion = "0.8.8"
}

/**
 * Publishing
 */
// bintray {
//    user = findProperty("bintrayUser") as String? ?: System.getenv("BINTRAY_USERNAME")
//    key = findProperty("bintrayKey") as String? ?: System.getenv("BINTRAY_API_KEY")
//    override = true
//
//    this.setPublications("release")
//
//    with(pkg) {
//        userOrg = "snowe"
//        repo = "maven"
//        name = "Pretty-Print"
//        desc = "Pretty printing of objects"
//
//        setLicenses("MIT")
//        websiteUrl = "https://github.com/$githubUsername/${project.name}"
//        issueTrackerUrl = "https://github.com/$githubUsername/${project.name}/issues"
//        vcsUrl = "https://github.com/$githubUsername/${project.name}.git"
//        setLabels("kotlin")
//        with(version) {
//            this.name = Ci.publishVersion
//            with(gpg) {
//                sign = true
//                passphrase = findProperty("gpgPassphrase") as String? ?: System.getenv("GPG_PASSPHRASE")
//            }
//            with(mavenCentralSync) {
//                user = findProperty("sonatypeUser") as String?
//                    ?: System.getenv("OSSRH_USERNAME") //OSS user token: mandatory
//                password = findProperty("sonatypePassword") as String?
//                    ?: System.getenv("OSSRH_PASSWORD") //OSS user password: mandatory
//            }
//        }
//    }
//    dryRun = false
//    publish = true
// }

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
        register<MavenPublication>("release") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }

    publications.withType<MavenPublication>().forEach {
        it.apply {
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
