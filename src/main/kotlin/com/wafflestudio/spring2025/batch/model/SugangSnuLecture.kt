package com.wafflestudio.spring2025.batch.model

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester

data class SugangSnuLecture(
    val id: Long,
    val academicYear: Int,
    val semester: Semester,
    val lectureType: String,
    val college: String,
    val department: String,
    val target: String,
    val grade: Int,
    val courseNumber: String,
    val lectureNumber: String,
    val title: String,
    val subtitle: String,
    val credit: Int,
    val lecturer: String,
    val schedule: List<LectureSchedule>,
)
