package com.wafflestudio.spring2025.user.dto.core

import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저")
data class UserDto(
    @Schema(description = "유저 ID") val id: Long,
    @Schema(description = "비밀번호") val username: String,
) {
    constructor(user: User) : this(user.id!!, user.username)
}
