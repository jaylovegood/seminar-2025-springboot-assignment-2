package com.wafflestudio.spring2025.timetable.service

import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.timetable.TimetableNameBlankException
import com.wafflestudio.spring2025.timetable.TimetableNameConflictException
import com.wafflestudio.spring2025.timetable.TimetableNotFoundException
import com.wafflestudio.spring2025.timetable.TimetableUpdateForbiddenException
import com.wafflestudio.spring2025.timetable.dto.core.TimetableDto
import com.wafflestudio.spring2025.timetable.model.Timetable
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.user.model.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TimetableService(
    private val timetableRepository: TimetableRepository,
    private val timetableLectureRepository: TimetableLectureRepository,
) {
    fun list(userId: Long): List<TimetableDto> =
        timetableRepository
            .findAllByUserId(userId)
            .map { TimetableDto(it) }

    fun get(
        user: User,
        id: Long,
    ): TimetableDto {
        val timetable =
            timetableRepository
                .findByIdOrNull(id)
                ?: throw TimetableNotFoundException()

        return TimetableDto(timetable, user)
    }

    fun create(
        user: User,
        timetableName: String,
        year: Int,
        semester: Semester,
    ): TimetableDto {
        if (timetableName.isBlank()) {
            throw TimetableNameBlankException()
        }
        if (timetableRepository.existsByTimetableName(timetableName)) {
            throw TimetableNameConflictException()
        }
        val timetable =
            timetableRepository.save(
                Timetable(
                    userId = user.id!!,
                    timetableName = timetableName,
                    year = year,
                    semester = semester,
                ),
            )
        return TimetableDto(timetable, user)
    }

    fun update(
        id: Long,
        user: User,
        timetableName: String,
    ): TimetableDto {
        if (timetableName.isBlank()) {
            throw TimetableNameBlankException()
        }

        val timetable =
            timetableRepository.findByIdOrNull(id)
                ?: throw TimetableNotFoundException()

        if (timetable.userId != user.id) {
            throw TimetableUpdateForbiddenException()
        }

        timetable.timetableName = timetableName
        val updatedTimetable = timetableRepository.save(timetable)
        return TimetableDto(updatedTimetable, user)
    }

    fun delete(
        id: Long,
        user: User,
    ) {
        val timetable =
            timetableRepository.findByIdOrNull(id)
                ?: throw TimetableNotFoundException()

        if (timetable.userId != user.id) {
            throw TimetableUpdateForbiddenException()
        }
        timetableLectureRepository.deleteAllByTimetableId(timetable.id!!)
        timetableRepository.delete(timetable)
    }
}
