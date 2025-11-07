package com.wafflestudio.spring2025.batch.mapping

import com.wafflestudio.spring2025.common.Semester

object SugangSnuMappings {
    fun semesterToSugangSnuSearchString(semester: Semester): String =
        when (semester) {
            Semester.SPRING -> "U000200001U000300001"
            Semester.SUMMER -> "U000200001U000300002"
            Semester.FALL -> "U000200002U000300001"
            Semester.WINTER -> "U000200002U000300002"
        }
}