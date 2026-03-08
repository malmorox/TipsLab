package app.iesjdlc.tipslab.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://qlopkjplziyzhwphfuah.supabase.co",
        supabaseKey = "sb_publishable_CaIRGEInQSz0rZ3BJXn9Bw_CrQFEznd"
    ) {
        install(Storage)
    }
}