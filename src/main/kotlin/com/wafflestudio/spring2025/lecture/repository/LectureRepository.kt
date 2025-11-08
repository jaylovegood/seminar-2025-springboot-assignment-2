package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LectureRepository : CrudRepository<Lecture, Long> {
    @Query(
        """
        SELECT *
        FROM lectures
        WHERE ((:nextId IS NULL) OR (id > :nextId))
        AND semester = :semester
        AND academic_year = :year
        AND ((title LIKE CONCAT('%', :keyword, '%')
        OR subtitle LIKE CONCAT('%', :keyword, '%')
        OR lecturer LIKE CONCAT('%', :keyword, '%')))
        ORDER BY id
        LIMIT :limit
    """,
    )
    fun getByKeywordWithCursor(
        @Param("keyword") keyword: String,
        @Param("year") year: Int,
        @Param("semester") semester: Semester,
        @Param("nextId") nextId: Long?,
        @Param("limit") limit: Int,
    ): List<Lecture>

    @Query(
        """
        SELECT day_of_week, start_time, end_time, location
        FROM lecture_time_place
        WHERE lecture_id = :id
    """,
    )
    fun getScheduleById(
        @Param("id") id: Long,
    ): List<LectureSchedule>

    @Query(
        """
        SELECT *
        FROM lecture_time_place
        WHERE lecture_id IN (:lectureIds)
    """,
    )
    fun getSchedulesByLectureIds(
        @Param("lectureIds") lectureIds: List<Long>,
    ): List<LectureTimePlace>
}
