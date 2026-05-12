package app.iesjdlc.tipslab.di

import android.content.Context
import androidx.work.WorkManager
import app.iesjdlc.tipslab.data.worker.WorkManagerUploadLifehackMediaEnqueuer
import app.iesjdlc.tipslab.domain.usecase.lifehack.boundary.UploadLifehackMediaEnqueuer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerBindsModule {
    @Binds
    abstract fun bindLifehackMediaEnqueuer(impl: WorkManagerUploadLifehackMediaEnqueuer): UploadLifehackMediaEnqueuer
}