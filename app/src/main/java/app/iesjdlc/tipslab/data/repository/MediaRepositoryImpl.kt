package app.iesjdlc.tipslab.data.repository

import android.content.Context
import android.net.Uri
import app.iesjdlc.tipslab.domain.model.MediaType
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
        mediaUri: Uri,
        mediaType: MediaType
    ): Result<String?> {
        return try {
            val bucket = storage.from("lifehack_media")

            val (fileBytes, fileName) = when (mediaType) {
                MediaType.IMAGE -> {
                    // Copiamos a fichero temporal y comprimimos
                    val tempFile = File(context.cacheDir, "lh_img_$lifehackId.jpg")
                    context.contentResolver.openInputStream(mediaUri)?.use { input ->
                        tempFile.outputStream().use { output -> input.copyTo(output) }
                    }
                    val compressed = Compressor.compress(context, tempFile) {
                        default(width = 1080, height = 1080, quality = 80)
                    }
                    val bytes = compressed.readBytes()
                    tempFile.delete()
                    compressed.delete()
                    bytes to "lh_${lifehackId}.jpg"
                }
                MediaType.VIDEO -> {
                    // Los vídeos no se comprimen, se suben directamente
                    val bytes = context.contentResolver
                        .openInputStream(mediaUri)
                        ?.use { it.readBytes() }
                        ?: return Result.failure(Exception("No se pudo leer el vídeo"))
                    bytes to "lh_${lifehackId}.mp4"
                }
            }

            bucket.upload(fileName, fileBytes, upsert = true)
            Result.success(bucket.publicUrl(fileName))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}