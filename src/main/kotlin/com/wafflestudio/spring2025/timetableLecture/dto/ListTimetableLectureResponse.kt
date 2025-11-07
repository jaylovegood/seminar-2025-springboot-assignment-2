package com.wafflestudio.spring2025.timetableLecture.dto

import com.wafflestudio.spring2025.timetableLecture.dto.core.TimetableLectureDto

data class ListTimetableLectureResponse(
    val lectures: List<TimetableLectureDto>,
    val grade: Int,
)
