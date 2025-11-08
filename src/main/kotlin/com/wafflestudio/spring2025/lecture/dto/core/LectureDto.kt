package com.wafflestudio.spring2025.lecture.dto.core

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture

data class LectureDto(
    val id: Long,
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
    val schedule: List<LectureSchedule>,
) {
    companion object {
        fun from(
            lecture: Lecture,
            schedules: List<LectureSchedule>,
        ): LectureDto =
            LectureDto(
                id = lecture.id!!,
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
                schedule = schedules,
            )
    }
}
