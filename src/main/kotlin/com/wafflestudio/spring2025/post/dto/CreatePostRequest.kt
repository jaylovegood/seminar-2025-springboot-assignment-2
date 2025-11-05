package com.wafflestudio.spring2025.post.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시물 생성 요청")
data class CreatePostRequest(
    @Schema(description = "게시물 제목") val title: String,
    @Schema(description = "게시물 내용") val content: String,
)
