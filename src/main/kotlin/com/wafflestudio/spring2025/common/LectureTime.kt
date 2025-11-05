package com.wafflestudio.spring2025.common

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

@Schema(
    description = "강의 시간",
)
data class LectureTime(
    @Schema(description = "강의 요일") val dayOfWeek: DayOfWeek,
    @Schema(description = "강의 시작 시간") val startTime: LocalTime,
    @Schema(description = "강의 종료 시간") val endTime: LocalTime,
    // LocalTime.parse() 로 파싱 (기본 포맷 HH:MM)
    // XLS 시간 포맷 따라서 ex. val formatter = DateTimeFormatter.ofPattern("H시 m분") 로 포맷 설정
)

@Schema(
    description = "요일",
    allowableValues = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"],
)
enum class DayOfWeek {
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN,
}
