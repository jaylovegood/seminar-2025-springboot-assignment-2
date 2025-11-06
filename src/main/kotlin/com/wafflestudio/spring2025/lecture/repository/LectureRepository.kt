package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.common.DayOfWeek
import com.wafflestudio.spring2025.common.LectureTime
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.model.LectureSchedule
import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import com.wafflestudio.spring2025.lecture.model.LectureWithSchedule
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

interface LectureRepository: CrudRepository<Lecture, Long>, LectureRepositoryCustom

@Repository
interface LectureRepositoryCustom {
    fun searchBySemesterAndKeyword(year: Int, semester: Semester, keyword: String): List<LectureWithSchedule>
}

class LectureRepositoryCustomImpl(
    private val jdbcTemplate: JdbcTemplate,
) : LectureRepositoryCustom {

    data class SearchResultRow(
        val lecture: Lecture,
        val schedule: LectureTimePlace?,
    )

    companion object {
        private const val SQL = """
            SELECT
                l.id AS id,
                academic_year,
                semester,
                lecture_type,
                college,
                department,
                target,
                grade,
                course_number,
                lecture_number,
                title,
                subtitle,
                credit,
                lecturer,
                day_of_week,
                start_time,
                end_time,
                location
            FROM lectures l
            LEFT JOIN lecture_time_place
            ON l.id = lecture_time_place.lecture_id
            WHERE academic_year = ?
            AND semester = ?
            AND (
                title LIKE ?
                OR subtitle LIKE ?
                OR lecturer LIKE ?
            )
            ORDER BY l.id
        """
    }

    private val searchResultRowMapper = RowMapper<SearchResultRow> { rs, _ ->
        val lecture = mapLecture(rs)
        val schedule = mapSchedule(rs)
        SearchResultRow(lecture, schedule)
    }

    private fun mapLecture(rs: ResultSet): Lecture {
        return Lecture(
            id = rs.getLong("id"),
            academicYear = rs.getInt("academic_year"),
            semester = Semester.valueOf(rs.getString("semester")),
            lectureType = rs.getString("lecture_type"),
            college = rs.getString("college"),
            department = rs.getString("department"),
            target = rs.getString("target"),
            grade = rs.getInt("grade"),
            courseNumber = rs.getString("course_number"),
            lectureNumber = rs.getString("lecture_number"),
            title = rs.getString("title"),
            subtitle = rs.getString("subtitle"),
            credit = rs.getInt("credit"),
            lecturer = rs.getString("lecturer"),
        )
    }

    private fun mapSchedule(rs: ResultSet): LectureTimePlace? {
        return if (rs.getString("day_of_week") != null) {
            LectureTimePlace(
                id = 0L,
                lectureId = rs.getLong("id"),
                time = LectureTime(
                    dayOfWeek = DayOfWeek.valueOf(rs.getString("day_of_week")),
                    startTime = rs.getTime("start_time").toLocalTime(),
                    endTime = rs.getTime("end_time").toLocalTime(),
                ),
                location = rs.getString("location"),
            )
        } else {
            null
        }
    }

    override fun searchBySemesterAndKeyword(year: Int, semester: Semester, keyword: String): List<LectureWithSchedule> {
        val searchPattern = "%${keyword}%"
        val rows: List<SearchResultRow> = jdbcTemplate.query(
            SQL,
            searchResultRowMapper,
            year,
            semester.name,
            searchPattern,
            searchPattern,
            searchPattern
        )

        return rows.groupBy { it.lecture.id }
            .values
            .map { grouped ->
                val lecture = grouped.first().lecture
                LectureWithSchedule(
                    lecture,
                    grouped
                        .mapNotNull { it.schedule }
                        .map { slot ->
                            LectureSchedule(
                                time = slot.time,
                                location = slot.location,
                            )
                        }
                )
            }
    }
}