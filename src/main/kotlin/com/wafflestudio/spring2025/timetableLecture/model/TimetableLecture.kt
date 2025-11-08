package com.wafflestudio.spring2025.timetableLecture.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("timetableLectures")
data class TimetableLecture(
    @Id
    val id: Long? = null,
    val timetableId: Long,
    val lectureId: Long,
)
