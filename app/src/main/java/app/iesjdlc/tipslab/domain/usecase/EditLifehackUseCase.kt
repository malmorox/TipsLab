package app.iesjdlc.tipslab.domain.usecase

import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import javax.inject.Inject

class EditLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {

}