package app.iesjdlc.tipslab.di

import app.iesjdlc.tipslab.data.datasource.local.CategoryLocalDataSource
import app.iesjdlc.tipslab.data.repository.AuthRepositoryImpl
import app.iesjdlc.tipslab.data.repository.CategoryRepositoryImpl
import app.iesjdlc.tipslab.data.repository.LifehackRepositoryImpl
import app.iesjdlc.tipslab.data.repository.MediaRepositoryImpl
import app.iesjdlc.tipslab.data.repository.UserRepositoryImpl
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import app.iesjdlc.tipslab.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindLifehackRepository(impl: LifehackRepositoryImpl): LifehackRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
