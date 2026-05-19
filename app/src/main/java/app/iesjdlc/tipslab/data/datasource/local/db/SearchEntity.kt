package app.iesjdlc.tipslab.data.datasource.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["user_id", "query"], unique = true)]
)
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)