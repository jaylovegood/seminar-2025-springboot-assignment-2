package com.wafflestudio.spring2025.common

import java.time.LocalTime

data class LectureTime(
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    // LocalTime.parse() 로 파싱 (기본 포맷 HH:MM)
    // XLS 시간 포맷 따라서 ex. val formatter = DateTimeFormatter.ofPattern("H시 m분") 로 포맷 설정
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
