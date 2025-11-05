package com.wafflestudio.spring2025.post.dto.core

import com.wafflestudio.spring2025.board.dto.core.BoardDto
import com.wafflestudio.spring2025.board.model.Board
import com.wafflestudio.spring2025.post.model.Post
import com.wafflestudio.spring2025.post.model.PostWithUserAndBoard
import com.wafflestudio.spring2025.user.dto.core.UserDto
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시물")
data class PostDto(
    @Schema(description = "게시물 ID") val id: Long,
    @Schema(description = "게시물 제목") val title: String,
    @Schema(description = "게시물 내용") val content: String,
    @Schema(description = "게시물 작성자") val user: UserDto,
    @Schema(description = "게시물이 작성된 게시판") val board: BoardDto,
    @Schema(description = "게시물 최초 작성 시각") val createdAt: Long,
    @Schema(description = "게시물 마지막 수정 시각") val updatedAt: Long,
) {
    constructor(post: Post, user: User, board: Board) : this(
        id = post.id!!,
        title = post.title,
        content = post.content,
        user = UserDto(user),
        board = BoardDto(board),
        createdAt = post.createdAt!!.toEpochMilli(),
        updatedAt = post.updatedAt!!.toEpochMilli(),
    )

    constructor(postWithUserAndBoard: PostWithUserAndBoard) : this(
        id = postWithUserAndBoard.id,
        title = postWithUserAndBoard.title,
        content = postWithUserAndBoard.content,
        user =
            UserDto(
                id = postWithUserAndBoard.user!!.id,
                username = postWithUserAndBoard.user.username,
            ),
        board =
            BoardDto(
                id = postWithUserAndBoard.board!!.id,
                name = postWithUserAndBoard.board.name,
            ),
        createdAt = postWithUserAndBoard.createdAt.toEpochMilli(),
        updatedAt = postWithUserAndBoard.updatedAt.toEpochMilli(),
    )
}
