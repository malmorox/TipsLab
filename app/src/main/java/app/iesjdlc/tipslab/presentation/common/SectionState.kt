package app.iesjdlc.tipslab.presentation.common

data class SectionState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)