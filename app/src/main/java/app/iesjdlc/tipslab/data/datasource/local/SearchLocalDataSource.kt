package app.iesjdlc.tipslab.data.datasource.local

import app.iesjdlc.tipslab.data.datasource.local.db.SearchDao
import javax.inject.Inject

class SearchLocalDataSource @Inject constructor(
    private val dao: SearchDao
) {

}