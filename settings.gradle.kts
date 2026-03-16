pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version("4.3.2")
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

develocity {
    buildScan {
        val userOptIn = providers.gradleProperty("buildscan").getOrElse("no")
        termsOfUseAgree = userOptIn
        publishing.onlyIf { userOptIn == "yes" }

        var isCI = System.getenv("CI").toBoolean()
        if (isCI) {
            termsOfUseAgree = "yes"
            publishing.onlyIf { true }
            uploadInBackground.set(false)
            tag("CI")
        }

        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
    }
}

rootProject.name = "pretty-print"


