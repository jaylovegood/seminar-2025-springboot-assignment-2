package com.wafflestudio.spring2025.lecture.model

import com.wafflestudio.spring2025.common.LectureTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalTime

@Table("lecture_time_place")
data class LectureTimePlace (
    var id: Long,
    var lectureId: Long,
    @Embedded(prefix = "time_", onEmpty = Embedded.OnEmpty.USE_NULL)
    val time: LectureTime,
    var location: String,
)