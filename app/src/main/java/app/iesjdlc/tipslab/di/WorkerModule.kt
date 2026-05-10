package app.iesjdlc.tipslab.di

import app.iesjdlc.tipslab.data.worker.WorkManagerUploadLifehackMediaEnqueuer
import app.iesjdlc.tipslab.domain.usecase.lifehack.boundary.UploadLifehackMediaEnqueuer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {
    @Binds
    abstract fun bindLifehackMediaEnqueuer(impl: WorkManagerUploadLifehackMediaEnqueuer): UploadLifehackMediaEnqueuer
}