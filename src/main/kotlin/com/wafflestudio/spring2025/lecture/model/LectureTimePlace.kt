package com.wafflestudio.spring2025.lecture.model

import com.wafflestudio.spring2025.common.LectureSchedule
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table

@Table("lecture_time_place")
data class LectureTimePlace(
    @Id var id: Long? = null,
    var lectureId: Long,
    @Embedded(prefix = "", onEmpty = Embedded.OnEmpty.USE_NULL)
    val schedule: LectureSchedule,
)
