package com.wafflestudio.spring2025.comment.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "댓글 생성 요청")
data class CreateCommentRequest(
    @Schema(description = "댓글 내용") val content: String,
)
