package com.wafflestudio.spring2025.board.dto.core

import com.wafflestudio.spring2025.board.model.Board
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시판")
data class BoardDto(
    @Schema(description = "게시판 ID") val id: Long,
    @Schema(description = "게시판 이름") val name: String,
) {
    constructor (board: Board) : this(
        id = board.id!!,
        name = board.name,
    )
}
