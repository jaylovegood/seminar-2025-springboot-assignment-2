package com.wafflestudio.spring2025.timetableLecture.dto.core

import com.wafflestudio.spring2025.lecture.dto.core.LectureDto
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.timetable.dto.core.TimetableDto
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLectureWithLecture
import com.wafflestudio.spring2025.user.dto.core.UserDto
import com.wafflestudio.spring2025.user.model.User

data class TimetableLectureDto(
    val id: Long,
    val lecture: LectureDto,
    val timetableId: Long,
){
    constructor(timetable: Timetable, timetableLecture: TimetableLecture, lecture: Lecture) : this(
        id = timetableLecture.id,
        //lecture = LectureDto()
        timetableId = timetable.id,
    )
    constructor(timetableLectureWithLecture: TimetableLectureWithLecture) : this(
        id = timetableLectureWithLecture.id,
        timetableId = timetableLectureWithLecture.id,
        //lecture = timetableLectureWithLecture.lecture,
    )
}
