package app.iesjdlc.tipslab.data.resolver

import app.iesjdlc.tipslab.data.mapper.CategoryMapper
import app.iesjdlc.tipslab.data.mapper.LifehackMapper
import app.iesjdlc.tipslab.data.mapper.UserMapper
import app.iesjdlc.tipslab.data.model.CategoryDto
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.data.model.UserDto
import app.iesjdlc.tipslab.domain.model.Lifehack
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifehackResolver @Inject constructor(
    private val db: FirebaseFirestore,
    private val lifehackMapper: LifehackMapper,
    private val categoryMapper: CategoryMapper,
    private val userMapper: UserMapper
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