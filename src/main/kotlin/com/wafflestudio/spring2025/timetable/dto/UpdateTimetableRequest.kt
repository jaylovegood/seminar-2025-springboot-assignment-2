package com.wafflestudio.spring2025.timetable.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시간표 수정 요청")
class UpdateTimetableRequest(
    @Schema(description = "수정할 시간표 이름", example = "수신망") var timetableName: String,
)
