package com.wafflestudio.spring2025.timetableLecture.dto.core

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.dto.core.LectureDto
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLectureWithLecture

data class TimetableLectureDto(
    val id: Long,
    val lecture: LectureDto,
    val timetableId: Long,
) {
    constructor(timetable: Timetable, timetableLecture: TimetableLecture, lecture: Lecture, schedules: List<LectureSchedule>) : this(
        id = timetableLecture.id!!,
        lecture = LectureDto.from(lecture, schedules),
        timetableId = timetable.id!!,
    )
    constructor(timetableLectureWithLecture: TimetableLectureWithLecture, schedules: List<LectureSchedule>) : this(
        id = timetableLectureWithLecture.id!!,
        timetableId = timetableLectureWithLecture.timetableId!!,
        lecture =
            LectureDto(
                id = timetableLectureWithLecture.lecture!!.lectureId,
                academicYear = timetableLectureWithLecture.lecture!!.academicYear,
                semester = timetableLectureWithLecture.lecture!!.semester,
                lectureType = timetableLectureWithLecture.lecture!!.lectureType,
                college = timetableLectureWithLecture.lecture!!.college,
                department = timetableLectureWithLecture.lecture!!.department,
                target = timetableLectureWithLecture.lecture!!.target,
                grade = timetableLectureWithLecture.lecture!!.grade,
                courseNumber = timetableLectureWithLecture.lecture!!.courseNumber,
                lectureNumber = timetableLectureWithLecture.lecture!!.lectureNumber,
                title = timetableLectureWithLecture.lecture!!.title,
                subtitle = timetableLectureWithLecture.lecture!!.subtitle,
                credit = timetableLectureWithLecture.lecture!!.credit,
                lecturer = timetableLectureWithLecture.lecture!!.lecturer,
                schedule = schedules,
            ),
    )
}
