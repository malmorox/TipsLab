package app.iesjdlc.tipslab.utils

object StringUtils {
    fun buildUniqueGoogleUsername(displayName: String?, email: String?): String {
        val base = when {
            !displayName.isNullOrBlank() -> displayName
            !email.isNullOrBlank() -> email.substringBefore("@")
            else -> "user"
        }

        return base
            .trim()
            .lowercase()
            .replace(" ", "")
            .replace(Regex("[^a-z0-9._]"), "")
            .take(20)
            .ifEmpty { "user" }
    }
}