package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.presentation.common.SectionState

@Composable
fun <T> ContentListSection(
    title: String,
    sectionState: SectionState<List<T>>,
    emptyMessage: String,
    skeletonItem: @Composable () -> Unit = { LifehackSectionListItemSkeleton() },
    itemContent: @Composable (T) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            sectionState.data == null -> SkeletonContent(
                skeletonItem = skeletonItem
            )

            sectionState.error != null -> ErrorContent(
                errorMessage = sectionState.error
            )

            sectionState.data.isEmpty() -> EmptyContent(
                emptyMessage = emptyMessage
            )

            else -> SectionList(
                items = sectionState.data,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun SkeletonContent(
    skeletonItem: @Composable () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(5) {
            skeletonItem()
        }
    }
}

@Composable
private fun ErrorContent(
    errorMessage: String?
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = errorMessage ?: stringResource(R.string.error_occurred),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun EmptyContent(
    emptyMessage: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = emptyMessage,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
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