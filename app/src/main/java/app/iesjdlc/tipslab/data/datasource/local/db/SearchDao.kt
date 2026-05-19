package app.iesjdlc.tipslab.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.iesjdlc.tipslab.core.constants.DBConstants

@Dao
interface SearchDao {
    @Query("SELECT * FROM ${DBConstants.Local.SEARCH_HISTORY_TABLE} " +
            "WHERE ${DBConstants.Local.USER_ID_FIELD} = :userId " +
            "ORDER BY ${DBConstants.Local.TIMESTAMP_FIELD} DESC " +
            "LIMIT 10")
    suspend fun getRecentSearches(userId: String): List<SearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: SearchEntity)

    @Query("DELETE FROM ${DBConstants.Local.SEARCH_HISTORY_TABLE} " +
            "WHERE ${DBConstants.Local.USER_ID_FIELD} = :userId")
    suspend fun clearAll(userId: String)
}