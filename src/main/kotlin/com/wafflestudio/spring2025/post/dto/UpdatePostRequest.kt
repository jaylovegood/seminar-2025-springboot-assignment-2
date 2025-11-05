package com.wafflestudio.spring2025.post.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시물 수정 요청")
data class UpdatePostRequest(
    @Schema(description = "수정할 게시물 제목") val title: String?,
    @Schema(description = "수정할 게시물 내용") val content: String?,
)
