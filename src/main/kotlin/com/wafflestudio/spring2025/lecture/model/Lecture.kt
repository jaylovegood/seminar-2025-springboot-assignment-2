package com.wafflestudio.spring2025.lecture.model

import com.wafflestudio.spring2025.common.Semester
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("lectures")
data class Lecture(
    @Id val id: Long? = null,
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
)
