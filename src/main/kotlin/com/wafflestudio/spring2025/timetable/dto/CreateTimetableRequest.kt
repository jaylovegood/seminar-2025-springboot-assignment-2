package com.wafflestudio.spring2025.timetable.dto

import com.wafflestudio.spring2025.common.Semester
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시간표 생성 요청")
class CreateTimetableRequest(
    @Schema(description = "시간표 이름") val timetableName: String,
    @Schema(description = "연도") val year: Int,
    @Schema(description = "학기") val semester: Semester,
)
