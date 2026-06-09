package app.iesjdlc.tipslab.domain.model

data class CommentWithOwnership(
    val comment: Comment,
    val isOwn: Boolean
)