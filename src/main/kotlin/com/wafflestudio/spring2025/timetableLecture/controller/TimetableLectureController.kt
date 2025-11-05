package com.wafflestudio.spring2025.timetableLecture.controller

import com.wafflestudio.spring2025.timetable.dto.CreateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.CreateTimetableResponse
import com.wafflestudio.spring2025.timetableLecture.dto.CreateTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.dto.core.TimetableLectureDto
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.timetableLecture.service.TimetableLectureService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.swing.text.html.parser.Entity

@RestController
@RequestMapping("/timetables/{timetableId}/timetableLectures")
class TimetableLectureController(
    private val timetableLectureRepository: TimetableLectureRepository,
    private val timetableLectureService: TimetableLectureService
){
    @PatchMapping("/{lectureId}")
    fun update(
        @PathVariable timetableId: Long,
        @PathVariable lectureId: Long,
    ): ResponseEntity<CreateTimetableLectureResponse> {
        val tldto = timetableLectureService.create(lectureId, timetableId)
        return ResponseEntity.ok(tldto)
    }
    @DeleteMapping("/{timetableLectureId}")
    fun delete(
        @PathVariable timetableLectureId: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit>{
        timetableLectureService.delete(timetableLectureId, user)
        return ResponseEntity.noContent().build()
    }

}
