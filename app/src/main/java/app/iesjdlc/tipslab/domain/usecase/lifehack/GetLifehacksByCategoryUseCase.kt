package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import javax.inject.Inject

data class CategorySectionLifehacks(
    val recent: List<Lifehack>,
    val popular: List<Lifehack>
)

class GetLifehacksByCategoryUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {
    suspend operator fun invoke(categoryId: Int): Result<CategorySectionLifehacks> = runCatching {


        CategorySectionLifehacks(
            recent = ,
            popular =
        )
    }
}