package app.iesjdlc.tipslab.data.datasource.local

import android.content.Context
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.data.model.CategoryDto
import app.iesjdlc.tipslab.data.repository.boundary.CategoryDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CategoryDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    private val _categories: List<CategoryDto> by lazy {
        context.resources.openRawResource(R.raw.categories)
            .bufferedReader()
            .use { it.readText() }
            .let { json.decodeFromString(it) }
    }

    override fun getCategories(): List<CategoryDto> = _categories

    override fun getById(id: String): CategoryDto? = _categories.find { it.id == id }
}