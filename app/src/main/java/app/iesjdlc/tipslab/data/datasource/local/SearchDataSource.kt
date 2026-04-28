package app.iesjdlc.tipslab.data.datasource.local

interface SearchDataSource {
    fun getSearchHistory(): List<String>
    fun saveSearch(query: String)
    fun clearHistory()
}