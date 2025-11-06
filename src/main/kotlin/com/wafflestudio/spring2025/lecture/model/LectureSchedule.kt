package com.wafflestudio.spring2025.lecture.model

import com.wafflestudio.spring2025.common.LectureTime

data class LectureSchedule(
    val time: LectureTime,
    val location: String,
)