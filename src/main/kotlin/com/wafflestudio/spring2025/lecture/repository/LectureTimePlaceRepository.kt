package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LectureTimePlaceRepository : CrudRepository<LectureTimePlace, Long> {
}