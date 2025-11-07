package com.wafflestudio.spring2025.helper

import com.wafflestudio.spring2025.board.model.Board
import com.wafflestudio.spring2025.board.repository.BoardRepository
import com.wafflestudio.spring2025.comment.model.Comment
import com.wafflestudio.spring2025.comment.repository.CommentRepository
import com.wafflestudio.spring2025.common.DayOfWeek
import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import com.wafflestudio.spring2025.post.model.Post
import com.wafflestudio.spring2025.post.repository.PostRepository
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.user.JwtTokenProvider
import com.wafflestudio.spring2025.user.model.User
import com.wafflestudio.spring2025.user.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component
import java.time.LocalTime
import kotlin.random.Random

@Component
class DataGenerator(
    private val userRepository: UserRepository,
    private val boardRepository: BoardRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val timetableRepository: TimetableRepository,
    private val lectureRepository: LectureRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val timetableLectureRepository: TimetableLectureRepository,
) {
    private val startCandidates = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(10, 30),
        LocalTime.of(13, 0),
        LocalTime.of(15, 0),
        LocalTime.of(16, 30),
        LocalTime.of(18, 0),
    )
    private val durations = listOf(60L, 75L, 90L, 120L)

    private fun overlap(aStart: LocalTime, aEnd: LocalTime, bStart: LocalTime, bEnd: LocalTime) =
        aStart < bEnd && bStart < aEnd

    fun generateUser(
        username: String? = null,
        password: String? = null,
    ): Pair<User, String> {
        val user =
            userRepository.save(
                User(
                    username = username ?: "user-${Random.Default.nextInt(1000000)}",
                    password = BCrypt.hashpw(password ?: "password-${Random.Default.nextInt(1000000)}", BCrypt.gensalt()),
                ),
            )
        return user to jwtTokenProvider.createToken(user.username)
    }

    fun generateBoard(name: String? = null): Board {
        val board =
            boardRepository.save(
                Board(
                    name = name ?: "board-${Random.Default.nextInt(1000000)}",
                ),
            )
        return board
    }

    fun generatePost(
        title: String? = null,
        content: String? = null,
        user: User? = null,
        board: Board? = null,
    ): Post {
        val post =
            postRepository.save(
                Post(
                    title = title ?: "title-${Random.Default.nextInt(1000000)}",
                    content = content ?: "content-${Random.Default.nextInt(1000000)}",
                    userId = (user ?: generateUser().first).id!!,
                    boardId = (board ?: generateBoard()).id!!,
                ),
            )
        return post
    }

    fun generateComment(
        content: String? = null,
        user: User? = null,
        post: Post? = null,
    ): Comment {
        val comment =
            commentRepository.save(
                Comment(
                    content = content ?: "content-${Random.Default.nextInt(1000000)}",
                    userId = (user ?: generateUser().first).id!!,
                    postId = (post ?: generatePost()).id!!,
                ),
            )
        return comment
    }

    fun generateTimetable(
        user: User? = null,
        timetableName: String? = null,
        year: Int? = null,
        semester: Semester? = null,
    ): Timetable {
        val timetable =
            timetableRepository.save(
                Timetable(
                    userId = (user ?: generateUser().first).id!!,
                    timetableName = timetableName ?: "timetable-${Random.Default.nextInt(1000000)}",
                    year = year ?: Random.Default.nextInt(2020, 2031),
                    semester = semester ?: Semester.entries.random(),
                ),
            )
        return timetable
    }

    fun generateLecture(
        academicYear: Int? = null,
        semester: Semester? = null,
        lectureType: String? = null,
        college: String? = null,
        department: String? = null,
        target: String? = null,
        grade: Int? = null,
        courseNumber: String? = null,
        lectureNumber: String? = null,
        title: String? = null,
        subtitle: String? = null,
        credit: Int? = null,
        lecturer: String? = null,
    ): Lecture {
        val lecture =
            lectureRepository.save(
                Lecture(
                    academicYear = academicYear ?: Random.Default.nextInt(2020, 2025),
                    semester = semester ?: Semester.entries.random(),
                    lectureType = lectureType ?: arrayOf("전선", "전필", "교양", "교직", "논문").random(),
                    college = college ?: "college-${Random.Default.nextInt(10)}",
                    department = department ?: "department-${Random.Default.nextInt(100)}",
                    target = target ?: arrayOf("학부", "대학원").random(),
                    grade = grade ?: Random.Default.nextInt(1, 5),
                    courseNumber = courseNumber ?: "course-${Random.Default.nextInt(10000000)}",
                    lectureNumber = lectureNumber ?: "lecture-${Random.Default.nextInt(10000000)}",
                    title = title ?: "title-${Random.Default.nextInt(10000000)}",
                    subtitle = subtitle ?: "subtitle-${Random.Default.nextInt(10000000)}",
                    credit = credit ?: Random.Default.nextInt(1, 5),
                    lecturer = lecturer ?: "lecturer-${Random.Default.nextInt(1000)}",
                ),
            )
        return lecture
    }
    fun generateNonOverlappingSchedules(
        count: Int = 2,
        day: DayOfWeek = DayOfWeek.MON,
        durationMinutes: Long = 75L,
    ): List<LectureSchedule> {
        val schedules = mutableListOf<LectureSchedule>()
        for (i in 0 until count) {
            val start = startCandidates.firstOrNull { s ->
                val end = s.plusMinutes(durationMinutes)
                schedules.none { p -> overlap(p.startTime, p.endTime, s, end) }
            } ?: LocalTime.of(20, 0)
            schedules += LectureSchedule(
                dayOfWeek = day,
                startTime = start,
                endTime = start.plusMinutes(durationMinutes),
                location = "B-${Random.nextInt(10)} R-${Random.nextInt(300)}",
            )
        }
        return schedules
    }
    fun generateOverlappingSchedules(
        day: DayOfWeek = DayOfWeek.MON,
        baseStart: LocalTime = LocalTime.of(9, 0),
        baseDurationMinutes: Long = 90L,
        shiftMinutes: Long = 15L,
    ): List<LectureSchedule> {
        val baseEnd = baseStart.plusMinutes(baseDurationMinutes)
        val s1 = LectureSchedule(
            dayOfWeek = day,
            startTime = baseStart,
            endTime = baseEnd,
            location = "B-1 R-101",
        )
        val s2 = LectureSchedule(
            dayOfWeek = day,
            startTime = baseStart.plusMinutes(shiftMinutes),
            endTime = baseEnd.plusMinutes(shiftMinutes),
            location = "B-1 R-101",
        )
        return listOf(s1, s2)
    }
    fun generateNonOverlappingWith(
        existing: List<LectureSchedule>,
        preferDay: DayOfWeek = DayOfWeek.MON,
        durationMinutes: Long = 75L,
    ): LectureSchedule {
        val start = startCandidates.firstOrNull { s ->
            val e = s.plusMinutes(durationMinutes)
            existing.none { ex ->
                ex.dayOfWeek == preferDay && overlap(ex.startTime, ex.endTime, s, e)
            }
        } ?: LocalTime.of(20, 0)
        return LectureSchedule(
            dayOfWeek = preferDay,
            startTime = start,
            endTime = start.plusMinutes(durationMinutes),
            location = "B-${Random.nextInt(10)} R-${Random.nextInt(300)}",
        )
    }

    /** ✅ 기존 스케줄 리스트와 겹치게 새 스케줄 하나 생성 */
    fun generateOverlappingWith(
        existing: List<LectureSchedule>,
        preferDay: DayOfWeek = DayOfWeek.MON,
        shiftMinutes: Long = 15L,
    ): LectureSchedule {
        val base = existing.firstOrNull { it.dayOfWeek == preferDay } ?: existing.first()
        return LectureSchedule(
            dayOfWeek = base.dayOfWeek,
            startTime = base.startTime.plusMinutes(shiftMinutes),
            endTime = base.endTime.plusMinutes(shiftMinutes),
            location = base.location,
        )
    }
    fun insertTimetableLecture(timetable: Timetable, lecture: Lecture) {
        timetableLectureRepository.save(
            TimetableLecture(
                timetableId = timetable.id!!,
                lectureId = lecture.id!!,
            )
        )
        // schedules는 DB엔 안 넣어도 괜찮음 — 검증용 DTO에서만 사용
    }

}


