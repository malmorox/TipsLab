package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.utils.SupabaseClient
import io.github.jan.supabase.storage.storage

class MediaRepository {
    private val storage = SupabaseClient.supabase.storage
}