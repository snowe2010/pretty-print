/**
 * Used under Apache 2 License from https://github.com/kotest/kotest/blob/master/buildSrc/src/main/kotlin/Ci.kt
 */
object Ci {

   // this is the version used for building snapshots
   // .buildnumber-snapshot will be appended
   private const val snapshotBase = "4.3.0"

   private val githubBuildNumber = System.getenv("GITHUB_RUN_NUMBER")

   private val snapshotVersion = when (githubBuildNumber) {
      null -> "$snapshotBase-LOCAL"
      else -> "$snapshotBase.${githubBuildNumber}-SNAPSHOT"
   }

   private val releaseVersion = System.getenv("RELEASE_VERSION")

   val isRelease = releaseVersion != null
   val publishVersion = releaseVersion ?: snapshotVersion
}
