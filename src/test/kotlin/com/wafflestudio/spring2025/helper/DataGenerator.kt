package com.wafflestudio.spring2025.helper

import com.wafflestudio.spring2025.board.model.Board
import com.wafflestudio.spring2025.board.repository.BoardRepository
import com.wafflestudio.spring2025.comment.model.Comment
import com.wafflestudio.spring2025.comment.repository.CommentRepository
import com.wafflestudio.spring2025.common.DayOfWeek
import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import com.wafflestudio.spring2025.lecture.repository.LectureTimePlaceRepository
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
    private val lecturetimeplaceRepository: LectureTimePlaceRepository,
) {
    private val startCandidates =
        listOf(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(13, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 30),
            LocalTime.of(18, 0),
        )
    private val durations = listOf(60L, 75L, 90L, 120L)

    private fun overlap(
        aStart: LocalTime,
        aEnd: LocalTime,
        bStart: LocalTime,
        bEnd: LocalTime,
    ) = aStart < bEnd && bStart < aEnd

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

    fun generateLectureTimePlace(lectureId: Long): LectureTimePlace {
        // 임의의 요일 하나 선택
        val day = DayOfWeek.values().random()

        // 1~10교시 중 랜덤 시간 (1교시 = 9:00)
        val startHour = 9 + Random.nextInt(0, 8)
        val startTime = LocalTime.of(startHour, 0)
        val endTime = startTime.plusMinutes(90)

        // 랜덤 강의실 이름
        val location = listOf("301동 101호", "302동 205호", "공학관 401", "사범대 201", "인문관 102").random()

        // LectureSchedule 객체 생성
        val schedule =
            LectureSchedule(
                dayOfWeek = day,
                startTime = startTime,
                endTime = endTime,
                location = location,
            )

        return lecturetimeplaceRepository.save(
            LectureTimePlace(
                id = null,
                lectureId = lectureId,
                schedule = schedule,
            ),
        )
    }

    fun generateLectureTimePlacefix(
        lectureId: Long,
        schedule: LectureSchedule,
    ): LectureTimePlace =
        lecturetimeplaceRepository.save(
            LectureTimePlace(
                lectureId = lectureId,
                schedule = schedule,
            ),
        )

    fun insertTimetableLecture(
        timetable: Timetable,
        lecture: Lecture,
    ): TimetableLecture =
        timetableLectureRepository.save(
            TimetableLecture(
                timetableId = timetable.id!!,
                lectureId = lecture.id!!,
            ),
        )
}
