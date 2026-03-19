package app.iesjdlc.tipslab.utils

import app.iesjdlc.tipslab.mappers.CategoryMapper
import app.iesjdlc.tipslab.mappers.LifehackMapper
import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.dto.CategoryDto
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.models.dto.UserDto
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LifehackResolver(
    private val db: FirebaseFirestore = FirebaseClient.db,
    private val lifehackMapper: LifehackMapper = LifehackMapper(),
    private val categoryMapper: CategoryMapper = CategoryMapper(),
    private val userMapper: UserMapper = UserMapper()
) {
    suspend fun resolve(dtos: List<LifehackDto>): List<Lifehack> {
        if (dtos.isEmpty()) return emptyList()

        val categoryIds = dtos.map { it.category_id }.distinct()
        val authorIds = dtos.map { it.author_id }.distinct()

        val categoriesById = db.collection("categories")
            .whereIn(FieldPath.documentId(), categoryIds)
            .get().await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(CategoryDto::class.java)
                    ?.let { doc.id to categoryMapper.toDomain(it) }
            }.toMap()

        val usersById = db.collection("users")
            .whereIn(FieldPath.documentId(), authorIds)
            .get().await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(UserDto::class.java)
                    ?.let { doc.id to userMapper.toDomain(it) }
            }.toMap()

        return dtos.mapNotNull { dto ->
            val category = categoriesById[dto.category_id] ?: return@mapNotNull null
            val author = usersById[dto.author_id] ?: return@mapNotNull null
            lifehackMapper.toDomain(dto, category, author)
        }
    }

    suspend fun resolveOne(dto: LifehackDto): Lifehack? = resolve(listOf(dto)).firstOrNull()
}