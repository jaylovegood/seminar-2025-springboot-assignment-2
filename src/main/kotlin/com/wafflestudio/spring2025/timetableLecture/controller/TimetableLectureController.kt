package com.wafflestudio.spring2025.timetableLecture.controller

import com.wafflestudio.spring2025.timetableLecture.dto.CreateTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.dto.GetTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.dto.ListTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.timetableLecture.service.TimetableLectureService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/timetables/{timetableId}/timetableLectures")
class TimetableLectureController(
    private val timetableLectureRepository: TimetableLectureRepository,
    private val timetableLectureService: TimetableLectureService,
) {
    @PostMapping("/{lectureId}")
    fun update(
        @PathVariable timetableId: Long,
        @PathVariable lectureId: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<CreateTimetableLectureResponse> {
        val tldto = timetableLectureService.create(lectureId, timetableId, user)
        return ResponseEntity.ok(tldto)
    }

    @DeleteMapping("/{timetableLectureId}")
    fun delete(
        @PathVariable timetableLectureId: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        timetableLectureService.delete(timetableLectureId, user)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun get(
        @PathVariable timetableId: Long,
    ): ResponseEntity<ListTimetableLectureResponse> = ResponseEntity.ok(timetableLectureService.get(timetableId))

    @GetMapping("/{timetableLectureId}")
    fun detail(
        @PathVariable timetableLectureId: Long,
    ): ResponseEntity<GetTimetableLectureResponse> = ResponseEntity.ok(timetableLectureService.detail(timetableLectureId))
}
