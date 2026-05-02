package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.presentation.common.SectionState

@Composable
fun <T> ContentListSection(
    title: String,
    sectionState: SectionState<List<T>>,
    emptyMessage: String,
    //skeletonContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            sectionState.data == null -> SkeletonContent()

            sectionState.error != null -> ErrorContent(errorMessage = sectionState.error)

            sectionState.data.isEmpty() -> EmptyContent(emptyMessage = emptyMessage)

            else -> SectionList(items = sectionState.data, itemContent = itemContent)
        }
    }
}

@Composable
private fun SkeletonContent() {

}

@Composable
private fun ErrorContent(errorMessage: String?) {

}

@Composable
private fun EmptyContent(
    emptyMessage: String
) {

}

@Composable
private fun <T> SectionList(
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(items) { item ->
            itemContent(item)
        }
    }
}