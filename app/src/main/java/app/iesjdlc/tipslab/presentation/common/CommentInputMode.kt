package app.iesjdlc.tipslab.presentation.common

sealed class CommentInputMode {
    object NewComment : CommentInputMode()
    data class Reply(val commentId: String) : CommentInputMode()
}