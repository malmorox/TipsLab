package app.iesjdlc.tipslab.di

import app.iesjdlc.tipslab.data.datasource.local.CategoryDataSource
import app.iesjdlc.tipslab.data.datasource.local.CategoryLocalDataSource
import app.iesjdlc.tipslab.data.datasource.remote.CommentDataSource
import app.iesjdlc.tipslab.data.datasource.remote.CommentRemoteDataSource
import app.iesjdlc.tipslab.data.datasource.remote.LifehackDataSource
import app.iesjdlc.tipslab.data.datasource.remote.LifehackRemoteDataSource
import app.iesjdlc.tipslab.data.datasource.remote.SavedLikedDataSource
import app.iesjdlc.tipslab.data.datasource.remote.SavedLikedRemoteDataSource
import app.iesjdlc.tipslab.data.datasource.remote.UserDataSource
import app.iesjdlc.tipslab.data.datasource.remote.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceBinder {
    @Binds
    @Singleton
    abstract fun bindCategoryDataSource(impl: CategoryLocalDataSource): CategoryDataSource

    @Binds
    @Singleton
    abstract fun bindCommentDataSource(impl: CommentRemoteDataSource): CommentDataSource

    @Binds
    @Singleton
    abstract fun bindLifehackDataSource(impl: LifehackRemoteDataSource): LifehackDataSource

    @Binds
    @Singleton
    abstract fun bindSavedLikedDataSource(impl: SavedLikedRemoteDataSource): SavedLikedDataSource

    @Binds
    @Singleton
    abstract fun bindUserDataSource(impl: UserRemoteDataSource): UserDataSource
}