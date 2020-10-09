/**
 * Used under Apache 2 License from https://github.com/kotest/kotest/blob/master/buildSrc/src/main/kotlin/Ci.kt
 */
object Ci {
   private const val snapshotBase = "2.0.8"
   private val githubBuildNumber = System.getenv("GITHUB_RUN_NUMBER")
   private val snapshotVersion = when (githubBuildNumber) {
      null -> "$snapshotBase-LOCAL"
      else -> "$snapshotBase.${githubBuildNumber}-SNAPSHOT"
   }

   private val releaseVersion = System.getenv("RELEASE_VERSION")

   val isRelease = releaseVersion != null && releaseVersion.isNotEmpty()
   val publishVersion = releaseVersion ?: snapshotVersion
}
