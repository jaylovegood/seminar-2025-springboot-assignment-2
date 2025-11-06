package com.wafflestudio.spring2025.lecture.model

import com.wafflestudio.spring2025.common.LectureTime
import com.wafflestudio.spring2025.common.Semester

data class LectureWithSchedule (
    val id: Long? = null,
    val academicYear: Int,
    val semester: Semester,
    val lectureType: String,
    val college: String,
    val department: String,
    val target: String,
    val grade: Int,
    val courseNumber: String,
    val lectureNumber: String,
    val title: String,
    val subtitle: String,
    val credit: Int,
    val lecturer: String,
    val schedules: List<LectureSchedule> = emptyList()
){
    constructor(lecture: Lecture, slots: List<LectureSchedule>) : this(
        id = lecture.id,
        academicYear = lecture.academicYear,
        semester = lecture.semester,
        lectureType = lecture.lectureType,
        college = lecture.college,
        department = lecture.department,
        target = lecture.target,
        grade = lecture.grade,
        courseNumber = lecture.courseNumber,
        lectureNumber = lecture.lectureNumber,
        title = lecture.title,
        subtitle = lecture.subtitle,
        credit = lecture.credit,
        lecturer = lecture.lecturer,
        schedules = slots.map {
            LectureSchedule(
                time = it.time,
                location = it.location,
            )
        },
    )
}