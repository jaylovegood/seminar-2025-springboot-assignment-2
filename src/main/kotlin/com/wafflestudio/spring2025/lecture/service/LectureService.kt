package com.wafflestudio.spring2025.lecture.service

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.dto.LecturePaging
import com.wafflestudio.spring2025.lecture.dto.LecturePagingResponse
import com.wafflestudio.spring2025.lecture.dto.core.LectureDto
import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import org.springframework.stereotype.Service

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
) {
    fun pageByKeyword(
        keyword: String,
        nextId: Long?,
        limit: Int,
    ): LecturePagingResponse {
        val lectures =
            lectureRepository.getByKeywordWithCursor(
                keyword = keyword,
                nextId = nextId,
                limit = limit + 1,
            )

        if (lectures.isEmpty()) {
            return LecturePagingResponse(
                data = emptyList(),
                paging = LecturePaging(nextId = null, hasNext = false),
            )
        }

        val lectureIds = lectures.map { it.id!! }
        val schedulesByLectureId =
            lectureRepository
                .getSchedulesByLectureIds(lectureIds)
                .groupBy { it.lectureId }

        val lectureDtos =
            lectures.map { lecture ->
                LectureDto(
                    id = lecture.id!!,
                    academicYear = lecture.academicYear,
                    semester = lecture.semester,
                    lectureType = lecture.lectureType,
                    college = lecture.college,
                    department = lecture.department,
                    target = lecture.target,
                    grade = lecture.grade,
                    courseNumber = lecture.courseNumber,
                    lectureNumber = lecture.lectureNumber,
                    title = lecture.title,
                    subtitle = lecture.subtitle,
                    credit = lecture.credit,
                    lecturer = lecture.lecturer,
                    schedule =
                        schedulesByLectureId[lecture.id]
                            ?.map {
                                LectureSchedule(
                                    dayOfWeek = it.schedule.dayOfWeek,
                                    startTime = it.schedule.startTime,
                                    endTime = it.schedule.endTime,
                                    location = it.schedule.location,
                                )
                            }?.sortedBy { it.dayOfWeek } ?: emptyList(),
                )
            }

        val hasNext = lectures.size > limit
        val pageLectures = if (hasNext) lectureDtos.subList(0, limit) else lectureDtos
        val nextCursorId = if (hasNext) lectureDtos.last().id else null

        return LecturePagingResponse(
            data = pageLectures,
            paging = LecturePaging(nextId = nextCursorId, hasNext = hasNext),
        )
    }
}
