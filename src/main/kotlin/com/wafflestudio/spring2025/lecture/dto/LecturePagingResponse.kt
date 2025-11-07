package com.wafflestudio.spring2025.lecture.dto

import com.wafflestudio.spring2025.lecture.dto.core.LectureDto

data class LecturePagingResponse (
    val data: List<LectureDto>,
    val paging: LecturePaging
)