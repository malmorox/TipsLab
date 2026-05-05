package app.iesjdlc.tipslab.domain.model

import android.net.Uri

sealed class MediaSource {
    data class Local(val uri: Uri, val type: MediaType) : MediaSource()
    data class Remote(val url: String, val type: MediaType) : MediaSource()

    val mediaType: MediaType
        get() = when (this) {
            is Local -> type
            is Remote -> type
        }
}