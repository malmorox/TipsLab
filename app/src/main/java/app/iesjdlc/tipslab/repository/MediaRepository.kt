package app.iesjdlc.tipslab.repository

import android.net.Uri
import android.util.Log
import app.iesjdlc.tipslab.utils.SupabaseClient
import io.github.jan.supabase.storage.storage

class MediaRepository {
    private val storage = SupabaseClient.supabase.storage

    suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): Result<String> {
        return Result.success("ok")
    }

    suspend fun uploadMediaToLifehack(lifehackId: String, mediaUri: Uri): Result<String> {
        return Result.success("ok")
    }
}