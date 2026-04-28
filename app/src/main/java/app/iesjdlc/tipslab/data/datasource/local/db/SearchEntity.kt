package app.iesjdlc.tipslab.data.datasource.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var query: String,
    var timestamp: Long = System.currentTimeMillis()
)