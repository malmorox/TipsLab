package app.iesjdlc.tipslab.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.iesjdlc.tipslab.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorSheet(
    categories: List<Category>,
    selectedCategory: Category?,
    expanded: Boolean,
    onToggle: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCategories = remember(searchQuery, categories) {
        if (searchQuery.isBlank()) categories
        else categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    // Cuando se cierra el dropdown, limpiamos la búsqueda
    LaunchedEffect(expanded) {
        if (!expanded) searchQuery = ""
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onToggle() },
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = if (expanded) searchQuery else selectedCategory?.name ?: "",
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            placeholder = { Text("Buscar categoría...") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onToggle() }
        ) {
            if (filteredCategories.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Sin resultados") },
                    onClick = {},
                    enabled = false
                )
            } else {
                filteredCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = { onCategorySelected(category) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}