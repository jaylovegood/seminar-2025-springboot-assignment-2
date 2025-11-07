package com.wafflestudio.spring2025.timetableLecture.repository

import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLectureWithLecture
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TimetableLectureRepository : CrudRepository<TimetableLecture, Long>{
    fun findByTimetableId(timetableId: Long): List<TimetableLecture>

    fun existsByTimetableIdAndLectureId(timetableId: Long, lectureId: Long): Boolean

    @Query(
        """
        SELECT 
            tl.id AS id,
            tl.timetable_id AS timetable_id,
            tl.lecture_id AS lecture_id,
            l.id AS lecture_lecture_id,
            l.academic_year AS lecture_academic_year,
            l.semester AS lecture_semester,
            l.lecture_type AS lecture_lecture_type,
            l.college AS lecture_college,
            l.department AS lecture_department,
            l.target AS lecture_target,
            l.grade AS lecture_grade,
            l.course_number AS lecture_course_number,
            l.lecture_number AS lecture_lecture_number,
            l.title AS lecture_title,
            l.subtitle AS lecture_subtitle,
            l.credit AS lecture_credit,
            l.lecturer AS lecture_lecturer
        FROM timetable_lecture tl
        JOIN lecture l ON tl.lecture_id = l.id
        WHERE tl.timetable_id = :timetableId
        """
    )

    fun findAllByTimetableIdWithLecture(timetableId: Long): List<TimetableLectureWithLecture>
    @Query("""
    SELECT COUNT(*) > 0
    FROM timetableLectures tl
    JOIN lecture_time_place ltp_existing ON ltp_existing.lecture_id = tl.lecture_id
    JOIN lecture_time_place ltp_new ON ltp_new.lecture_id = :lectureId
    WHERE tl.timetable_id = :timetableId
      AND ltp_existing.day_of_week = ltp_new.day_of_week
      AND (
            (ltp_existing.start_time < ltp_new.end_time)
        AND (ltp_new.start_time < ltp_existing.end_time)
      )
""")
    fun hasTimeConflict(
        @Param("timetableId") timetableId: Long,
        @Param("lectureId") lectureId: Long,
    ): Boolean


}
