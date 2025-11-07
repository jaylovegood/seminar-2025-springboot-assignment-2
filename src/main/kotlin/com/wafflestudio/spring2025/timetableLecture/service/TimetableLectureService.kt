package com.wafflestudio.spring2025.timetableLecture.service

import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import com.wafflestudio.spring2025.lecture.service.LectureService
import com.wafflestudio.spring2025.timetable.TimetableNotFoundException
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import com.wafflestudio.spring2025.timetable.service.TimetableService
import com.wafflestudio.spring2025.timetableLecture.AlreadyExistsException
import com.wafflestudio.spring2025.timetableLecture.ForbiddenException
import com.wafflestudio.spring2025.timetableLecture.LectureNotFoundException
import com.wafflestudio.spring2025.timetableLecture.TimetableLectureException
import com.wafflestudio.spring2025.timetableLecture.TimetableLectureNotFoundException
import com.wafflestudio.spring2025.timetableLecture.dto.core.TimetableLectureDto
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.user.model.User
import org.springframework.data.repository.Repository

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.collections.map
import kotlin.collections.sortedBy

@Service
class TimetableLectureService(
    private val timetableRepository: TimetableRepository,
    private val lectureRepository: LectureRepository,
    private val timetableLectureRepository: TimetableLectureRepository,

    ){
    fun create(
        lectureId: Long,
        timetableId: Long,
        ): TimetableLectureDto {
        val lecture = lectureRepository.findByIdOrNull(lectureId)
            ?: throw LectureNotFoundException()
        val timetable = timetableRepository.findByIdOrNull(timetableId)
            ?: throw TimetableNotFoundException()

        // 중복 여부 확인
        if (timetableLectureRepository.existsByTimetableIdAndLectureId(timetableId, lectureId)) {
            throw AlreadyExistsException()
        }

        // ✅ 여기서 save 실행
        val timetableLecture = TimetableLecture(
            lectureId = lecture.id!!,
            timetableId = timetable.id!!,
        )
        val saved = timetableLectureRepository.save(timetableLecture)

        val schedule = lectureRepository.getScheduleById(lectureId)

        return TimetableLectureDto(timetable, saved, lecture, schedule)
    }
    fun delete(timetableLectureId: Long,user: User){
        val timetableLecture = timetableLectureRepository.findByIdOrNull(timetableLectureId) ?: throw TimetableLectureNotFoundException()
        val timetable = timetableRepository.findByIdOrNull(timetableLecture.timetableId) ?: throw TimetableNotFoundException()
        if (user.id != timetable.userId){
            throw ForbiddenException()
        }
        timetableLectureRepository.delete(timetableLecture)
    }
    fun get(timetableId: Long): List<TimetableLectureDto> {
        if (!timetableRepository.existsById(timetableId)) {
            throw TimetableNotFoundException()
        }
        val timetableLectureWithLectures = timetableLectureRepository.findAllByTimetableIdWithLecture(timetableId)
        if (timetableLectureWithLectures.isEmpty()) {
            return emptyList()
        }
        val lectureIds = timetableLectureWithLectures.map { it.lectureId!! }
        val schedulesByLectureId =
            lectureRepository
                .getSchedulesByLectureIds(lectureIds)
                .groupBy { it.lectureId }
        val timetableLectureDtos = timetableLectureWithLectures.map { timetableLectureWithLecture ->
            val schedules = schedulesByLectureId[timetableLectureWithLecture.lectureId]
                ?.map {
                    LectureSchedule(
                        dayOfWeek = it.schedule.dayOfWeek,
                        startTime = it.schedule.startTime,
                        endTime = it.schedule.endTime,
                        location = it.schedule.location,
                    )
                }?.sortedWith(
                    compareBy<LectureSchedule> { it.dayOfWeek }
                        .thenBy { it.startTime }
                ) ?: emptyList()

            TimetableLectureDto(
                timetableLectureWithLecture = timetableLectureWithLecture,
                schedules = schedules,
            )
        }
        return timetableLectureDtos.sortedWith(
            compareBy<TimetableLectureDto> {
                it.lecture.schedule.minOfOrNull { s -> s.dayOfWeek.ordinal } ?: Int.MAX_VALUE
            }.thenBy {
                it.lecture.schedule.minOfOrNull { s -> s.startTime } ?: "99:99"
            }
        )
    }
}
