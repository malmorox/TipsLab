package app.iesjdlc.tipslab.core.constants

object DBConstants {
    // Colecciones
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
    const val CATEGORY_ID_FIELD = "category_id"
    const val STEPS_FIELD = "steps"
    const val MEDIA_URL_FIELD = "media_url"
    const val MEDIA_TYPE_FIELD = "media_type"
    const val CREATED_AT_FIELD = "created_at"
    const val UPDATED_AT_FIELD = "updated_at"

    // Campos comment
    const val COMMENTED_AT_FIELD = "commented_at"

    // Campos reply
    const val REPLIED_AT_FIELD = "replied_at"
}