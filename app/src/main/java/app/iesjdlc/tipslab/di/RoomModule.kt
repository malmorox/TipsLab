package app.iesjdlc.tipslab.di

import android.content.Context
import androidx.room.Room
import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.local.db.SearchDao
import app.iesjdlc.tipslab.data.datasource.local.db.TipsLabDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideTipsLabDatabase(
        @ApplicationContext context: Context
    ): TipsLabDatabase = Room.databaseBuilder(
        context,
        TipsLabDatabase::class.java,
        DBConstants.Local.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideSearchDao(database: TipsLabDatabase): SearchDao = database.searchDao()
}

