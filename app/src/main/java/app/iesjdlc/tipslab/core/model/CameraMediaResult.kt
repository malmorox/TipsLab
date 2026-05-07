package app.iesjdlc.tipslab.core.model

import android.net.Uri
import android.os.Parcelable
import app.iesjdlc.tipslab.domain.model.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CameraMediaResult(
    val uri: Uri,
    val type: MediaType
) : Parcelable