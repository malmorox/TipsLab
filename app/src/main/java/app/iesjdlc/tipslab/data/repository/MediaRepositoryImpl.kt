package app.iesjdlc.tipslab.data.repository

import android.content.Context
import android.net.Uri
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import io.github.jan.supabase.storage.Storage
import java.io.File
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val storage: Storage
) : MediaRepository {
    override suspend fun uploadProfilePhoto(uid: String, imageUri: Uri): Result<String> {
        return try {
            // Convertimos la URI a File de forma temporal
            val file = File(context.cacheDir, "temp_image_${uid}.jpg")
            context.contentResolver.openInputStream(imageUri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }

            // Comprimimos la imagen
            val compressedFile = Compressor.compress(context, file) {
                default(width = 320, height = 320, quality = 80)
            }

            // Lo subimos a Supabase (Bucket "profile_photos")
            val fileName = "pf_$uid.jpg"
            val bucket = storage.from("profile_photos")

            // Upsert true para que sobrescriba si ya existe
            bucket.upload(fileName, compressedFile.readBytes(), upsert = true)

            // Limpiamos archivos temporales
            file.delete()
            compressedFile.delete()

            Result.success(bucket.publicUrl(fileName))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadMediaToLifehack(
        lifehackId: String,
        mediaUri: Uri
    ): Result<String?> {
        return Result.success(null)
    }
}