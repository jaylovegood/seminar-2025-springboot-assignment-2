package com.wafflestudio.spring2025.timetable.model

import com.wafflestudio.spring2025.common.Semester
import org.springframework.data.relational.core.mapping.Embedded

data class TimetableWithUser(
    val id: Long,
    @Embedded.Nullable(prefix = "owner_")
    val user: User?,
    val timetableName: String,
    val year: Int,
    val semester: Semester,
) {
    data class User(
        val id: Long,
        val username: String,
    )
}
