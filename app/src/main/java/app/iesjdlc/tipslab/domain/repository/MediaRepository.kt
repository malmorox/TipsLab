package app.iesjdlc.tipslab.domain.repository

import android.net.Uri

interface MediaRepository {
    suspend fun uploadProfilePhoto(
        uid: String,
        imageUri: Uri
    ): Result<String>
    suspend fun uploadMediaToLifehack(
        lifehackId: String,
        mediaUri: Uri
    ): Result<String?>
}