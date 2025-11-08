package com.wafflestudio.spring2025.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "학기",
    allowableValues = ["SPRING", "SUMMER", "FALL", "WINTER"],
)
enum class Semester {
    SPRING,
    SUMMER,
    FALL,
    WINTER,
}
