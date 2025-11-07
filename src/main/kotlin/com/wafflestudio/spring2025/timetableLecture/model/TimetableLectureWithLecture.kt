package com.wafflestudio.spring2025.timetableLecture.model

import com.wafflestudio.spring2025.common.Semester
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Embedded

data class TimetableLectureWithLecture (
    @Id
    val id: Long,
    val timetableId: Long,
    val lectureId: Long,

    @Embedded.Nullable(prefix = "lecture_")
    val lecture: Lecture?,
){
    data class Lecture(
        val lectureId: Long,
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
        )
}