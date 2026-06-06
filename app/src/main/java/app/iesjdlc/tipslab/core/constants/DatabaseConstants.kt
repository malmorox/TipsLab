package app.iesjdlc.tipslab.core.constants

object DBConstants {
    object Local {
        const val DATABASE_NAME = "tipslab_database"
        const val SEARCH_HISTORY_TABLE = "search_history"

        // Campos search history
        const val ID_FIELD = "id"
        const val USER_ID_FIELD = "user_id"
        const val QUERY_FIELD = "query"
        const val TIMESTAMP_FIELD = "timestamp"
    }

    object Remote {
        // Colecciones de Firestore
        const val USERS_COLLECTION = "users"
        const val LIFEHACKS_COLLECTION = "lifehacks"
        const val COMMENTS_SUBCOLLECTION = "comments"
        const val REPLIES_SUBCOLLECTION = "replies"
        const val LIKED_SUBCOLLECTION = "liked"
        const val SAVED_SUBCOLLECTION = "saved"

        // Campos comunes
        const val ID_FIELD = "id"
        const val TEXT_FIELD = "text"
        const val AUTHOR_ID_FIELD = "author_id"
        const val LIKES_COUNT_FIELD = "likes_count"

        // Campos user
        const val EMAIL_FIELD = "email"
        const val USERNAME_FIELD = "username"
        const val PHOTO_URL_FIELD = "photo_url"
        const val PROVIDER_FIELD = "provider"

        // Campos lifehack
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val CATEGORY_ID_FIELD = "category_id"
        const val STEPS_FIELD = "steps"
        const val MEDIA_URL_FIELD = "media_url"
        const val MEDIA_TYPE_FIELD = "media_type"
        const val CREATED_AT_FIELD = "created_at"
        const val UPDATED_AT_FIELD = "updated_at"
        const val COMMENTS_COUNT_FIELD = "comments_count"

        // Campos comment
        const val COMMENTED_AT_FIELD = "commented_at"
        const val REPLIES_COUNT_FIELD = "replies_count"

        // Campos reply
        const val REPLIED_AT_FIELD = "replied_at"
    }
}