package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.model.Lecture
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LectureRepository: CrudRepository<Lecture, Long> {
    @Query("""
        SELECT *
        FROM lectures
        WHERE title LIKE '%' || :keyword || '%'
        OR subtitle LIKE '%' || :keyword || '%'
        OR lecturer LIKE '%' || :keyword || '%'
    """)
    fun getByKeyword(@Param("keyword") keyword: String): List<Lecture>

    @Query("""
        SELECT day_of_week, start_time, end_time, location
        FROM lecture_time_place
        WHERE lecture_id = :id
    """)
    fun getScheduleById(@Param("id") id: Long): List<LectureSchedule>
}
