package com.wafflestudio.spring2025.post.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "")
data class PostPaging(
    @Schema(description = "다음 페이지 요청 시 사용할 커서 기준 시간") val nextCreatedAt: Long?,
    @Schema(description = "다음 페이지 요청 시 사용할 마지막 게시물 ID") val nextId: Long?,
    @Schema(description = "다음 페이지 존재 여부") val hasNext: Boolean,
)
