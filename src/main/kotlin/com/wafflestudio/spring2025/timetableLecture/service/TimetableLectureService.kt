package com.wafflestudio.spring2025.timetableLecture.service

import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import com.wafflestudio.spring2025.lecture.service.LectureService
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import com.wafflestudio.spring2025.timetable.service.TimetableService
import com.wafflestudio.spring2025.timetableLecture.TimetableLectureException
import com.wafflestudio.spring2025.timetableLecture.TimetableLectureTestException
import com.wafflestudio.spring2025.timetableLecture.dto.core.TimetableLectureDto
import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.user.model.User
import org.springframework.data.repository.Repository

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

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
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw TimetableLectureTestException()
        val timetable = timetableRepository.findByIdOrNull(timetableId) ?: throw TimetableLectureTestException()
        val timetableLecture = TimetableLecture(
            lectureId = lecture.id!!,
            timetableId = timetable.id!!,
        )
        val schedule = lectureRepository.getScheduleById(lectureId) ?: throw TimetableLectureTestException()
        return TimetableLectureDto(timetable, timetableLecture,lecture,schedule)
    }
    fun delete(timetableLectureId: Long,user: User){
        val timetableLecture = timetableLectureRepository.findByIdOrNull(timetableLectureId) ?: throw TimetableLectureTestException()
        val timetable = timetableRepository.findByIdOrNull(timetableLecture.timetableId) ?: throw TimetableLectureTestException()
        if (user.id != timetable.id){
            throw TimetableLectureTestException()
        }
        timetableLectureRepository.delete(timetableLecture)


    }
}
