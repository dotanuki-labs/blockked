data class Version(
    val name: String,
    val major: Int,
    val minor: Int,
    val patch: Int,
    val code: Int
)

object Versioning {
    private const val major = 0
    private const val minor = 0
    private const val patch = 3
    private const val name = "$major.$minor.$patch"
    private const val code = 100 * major + 10 * minor + patch

    @JvmStatic fun getVersion() = Version(name, major, minor, patch, code)

}