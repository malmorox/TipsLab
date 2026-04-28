package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorSheet(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCategories = remember(searchQuery, categories) {
        if (searchQuery.isBlank()) categories
        else categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .padding(start = 24.dp, end = 24.dp, top = 36.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_category),
                style = MaterialTheme.typography.titleLarge,
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_category)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filteredCategories.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    filteredCategories.forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = category == selectedCategory,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}