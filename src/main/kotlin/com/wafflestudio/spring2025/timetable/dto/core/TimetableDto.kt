package com.wafflestudio.spring2025.timetable.dto.core

import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetable.model.TimetableWithUser
import com.wafflestudio.spring2025.user.dto.core.UserDto
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시간표")
data class TimetableDto(
    @Schema(description = "시간표 ID") val id: Long?,
    @Schema(description = "시간표 소유자") val user: UserDto,
    @Schema(description = "시간표 이름") val timetableName: String,
    @Schema(description = "연도") val year: Int,
    @Schema(description = "학기") val semester: Semester,
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
