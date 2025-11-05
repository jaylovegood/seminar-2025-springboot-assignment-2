package com.wafflestudio.spring2025.timetable.dto.core

import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetable.model.TimetableWithUser
import com.wafflestudio.spring2025.user.dto.core.UserDto
import com.wafflestudio.spring2025.user.model.User

data class TimetableDto(
    val id: Long?,
    val user: UserDto,
    val timetableName: String,
    val year: Int,
    val semester: Semester,
) {
    constructor(timetableWithUser: TimetableWithUser) : this(
        id = timetableWithUser.id,
        user =
            UserDto(
                id = timetableWithUser.user!!.id,
                username = timetableWithUser.user.username,
            ),
        timetableName = timetableWithUser.timetableName,
        year = timetableWithUser.year,
        semester = timetableWithUser.semester,
    )

    constructor(timetable: Timetable, user: User) : this(
        id = timetable.id,
        user = UserDto(user),
        timetableName = timetable.timetableName,
        year = timetable.year,
        semester = timetable.semester,
    )
}
