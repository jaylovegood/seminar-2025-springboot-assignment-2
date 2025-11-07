package com.wafflestudio.spring2025.timetable.repository

import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetable.model.TimetableWithUser
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface TimetableRepository : CrudRepository<Timetable, Long> {
    fun existsByTimetableName(timetableName: String): Boolean

    @Query(
        """
        SELECT 
            t.id AS id,
            t.user_id AS user_id,
            t.timetable_name AS timetable_name,
            t.year AS year,
            t.semester AS semester,
            u.id AS owner_id,
            u.username AS owner_username
        FROM timetables t
        JOIN users u ON u.id = t.user_id
        WHERE t.user_id = :userId
        """,
    )
    fun findAllByUserId(
        @Param("userId") userId: Long,
    ): List<TimetableWithUser>

}
