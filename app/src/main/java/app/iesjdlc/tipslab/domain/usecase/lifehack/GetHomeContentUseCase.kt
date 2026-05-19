package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {
    suspend operator fun invoke(

    ) {

    }
}