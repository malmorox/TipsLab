package app.iesjdlc.tipslab.presentation.screens.home

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.common.SectionState

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val sections: ContentSections = ContentSections()
) {
    val globalContentError: Boolean
        get() = sections.forYou.error != null
                && sections.trending.error != null
                && sections.recent.error != null

    val globalContentNoData: Boolean
        get() = sections.forYou.data.isNullOrEmpty()
                && sections.trending.data.isNullOrEmpty()
                && sections.recent.data.isNullOrEmpty()
}

data class ContentSections(
    val forYou: SectionState<List<Lifehack>> = SectionState(),
    val trending: SectionState<List<Lifehack>> = SectionState(),
    val recent: SectionState<List<Lifehack>> = SectionState()
)