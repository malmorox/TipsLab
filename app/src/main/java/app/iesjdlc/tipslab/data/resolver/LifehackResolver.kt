package app.iesjdlc.tipslab.data.resolver

import app.iesjdlc.tipslab.data.datasource.local.CategoryDataSource
import app.iesjdlc.tipslab.data.datasource.remote.UserDataSource
import app.iesjdlc.tipslab.data.mapper.CategoryMapper
import app.iesjdlc.tipslab.data.mapper.LifehackMapper
import app.iesjdlc.tipslab.data.mapper.UserMapper
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifehackResolver @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val userDataSource: UserDataSource,
    private val lifehackMapper: LifehackMapper,
    private val categoryMapper: CategoryMapper,
    private val userMapper: UserMapper
) {
    suspend fun resolve(dtos: List<LifehackDto>): List<Lifehack> {
        if (dtos.isEmpty()) return emptyList()

        val categoriesById = fetchCategories(dtos.map { it.categoryId }.distinct())
        val usersById = fetchUsers(dtos.map { it.authorId }.distinct())

        return dtos.mapNotNull { dto -> enrich(dto, categoriesById, usersById) }
    }

    suspend fun resolveOne(dto: LifehackDto): Lifehack? {
        val category = categoryDataSource.getById(dto.categoryId)
            ?.let { categoryMapper.toDomain(it) } ?: return null
        val author = userDataSource.getById(dto.authorId)
            ?.let { userMapper.toDomain(it) } ?: return null
        return lifehackMapper.toDomain(dto, category, author)
    }

    // Obtiene las categorías por sus IDs desde el JSON local y las mapea a dominio
    private fun fetchCategories(ids: List<Int>): Map<Int, Category> =
        ids.mapNotNull { id ->
            categoryDataSource.getById(id)?.let { id to categoryMapper.toDomain(it) }
        }.toMap()

    // Obtiene los usuarios por sus IDs desde Firestore en una sola query y los mapea a dominio.
    private suspend fun fetchUsers(ids: List<String>): Map<String, User> =
        userDataSource.getByIds(ids)
            .mapValues { (_, dto) -> userMapper.toDomain(dto) }

    // Construye un Lifehack de dominio a partir de un LifehackDto y los mapas de categorías y usuarios ya resueltos
    private fun enrich(
        dto: LifehackDto,
        categoriesById: Map<Int, Category>,
        usersById: Map<String, User>
    ): Lifehack? {
        val category = categoriesById[dto.categoryId] ?: return null
        val author = usersById[dto.authorId] ?: return null
        return lifehackMapper.toDomain(dto, category, author)
    }
}