package com.wafflestudio.spring2025.timetableLecture.model

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
    )
}