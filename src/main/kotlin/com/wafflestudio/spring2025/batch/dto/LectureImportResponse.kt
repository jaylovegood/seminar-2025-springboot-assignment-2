package com.wafflestudio.spring2025.batch.dto

import com.wafflestudio.spring2025.common.Semester

data class LectureImportResponse(
    val academicYear: Int,
    val semester: Semester,
    val addedCnt: Int,
)
