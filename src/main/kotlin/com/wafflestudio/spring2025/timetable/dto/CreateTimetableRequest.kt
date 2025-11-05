package com.wafflestudio.spring2025.timetable.dto

import com.wafflestudio.spring2025.common.Semester

class CreateTimetableRequest(
    val timetableName: String,
    val year: Int,
    val semester: Semester,
)
