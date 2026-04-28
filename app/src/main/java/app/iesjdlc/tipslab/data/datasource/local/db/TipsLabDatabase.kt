package app.iesjdlc.tipslab.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SearchEntity::class
    ],
    version = 1
)
abstract class TipsLabDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}