package com.wafflestudio.spring2025.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청")
data class LoginRequest(
    @Schema(description = "유저 이름") val username: String,
    @Schema(description = "비밀번호") val password: String,
)
