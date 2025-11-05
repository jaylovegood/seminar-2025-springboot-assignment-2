package com.wafflestudio.spring2025.comment.dto.core

import com.wafflestudio.spring2025.comment.model.Comment
import com.wafflestudio.spring2025.comment.model.CommentWithUser
import com.wafflestudio.spring2025.user.dto.core.UserDto
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "댓글")
data class CommentDto(
    @Schema(description = "댓글 ID") val id: Long?,
    @Schema(description = "댓글 내용") val content: String,
    @Schema(description = "댓글이 달린 게시물 ID") val postId: Long,
    @Schema(description = "댓글을 단 사용자") val user: UserDto,
    @Schema(description = "댓글 최초 작성 시각") val createdAt: Long,
    @Schema(description = "댓글 마지막 수정 시각") val updatedAt: Long,
) {
    constructor(comment: Comment, user: User) : this(
        id = comment.id,
        content = comment.content,
        postId = comment.postId,
        user = UserDto(user),
        createdAt = comment.createdAt!!.toEpochMilli(),
        updatedAt = comment.updatedAt!!.toEpochMilli(),
    )

    constructor(commentWithUser: CommentWithUser) : this(
        id = commentWithUser.id,
        content = commentWithUser.content,
        postId = commentWithUser.postId,
        user =
            UserDto(
                id = commentWithUser.user!!.id,
                username = commentWithUser.user.username,
            ),
        createdAt = commentWithUser.createdAt.toEpochMilli(),
        updatedAt = commentWithUser.updatedAt.toEpochMilli(),
    )
}
