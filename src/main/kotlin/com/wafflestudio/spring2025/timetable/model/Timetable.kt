package com.wafflestudio.spring2025.timetable.model

import com.wafflestudio.spring2025.common.Semester
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("timetables")
class Timetable(
    @Id var id: Long? = null,
    var userId: Long,
    var timetableName: String,
    var year: Int,
    var semester: Semester,
)
