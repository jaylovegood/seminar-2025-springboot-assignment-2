package com.wafflestudio.spring2025.timetable.controller

import com.wafflestudio.spring2025.timetable.dto.CreateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.CreateTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.core.TimetableDto
import com.wafflestudio.spring2025.timetable.service.TimetableService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/timetables")
class TimetableController(
    private val timetableService: TimetableService,
) {
    @PostMapping
    fun create(
        @RequestBody createRequest: CreateTimetableRequest,
        @LoggedInUser user: User,
    ): ResponseEntity<CreateTimetableResponse> {
        val timetable =
            timetableService.create(
                user = user,
                timetableName = createRequest.timetableName,
                year = createRequest.year,
                semester = createRequest.semester,
            )
        return ResponseEntity.ok(timetable)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @LoggedInUser user: User,
        @RequestBody updateRequest: UpdateTimetableRequest,
    ): ResponseEntity<UpdateTimetableResponse> {
        val timetable =
            timetableService.update(
                id = id,
                user = user,
                timetableName = updateRequest.timetableName,
            )
        return ResponseEntity.ok(timetable)
    }

    @GetMapping
    fun list(
        @LoggedInUser user: User,
    ): ResponseEntity<List<TimetableDto>> {
        val timetables =
            timetableService.list(
                userId = user.id!!,
            )
        return ResponseEntity.ok(timetables)
    }

    @GetMapping("/{id}")
    fun get(
        @LoggedInUser user: User,
        @PathVariable id: Long,
    ): ResponseEntity<TimetableDto> {
        val timetable =
            timetableService.get(
                user = user,
                id = id,
            )
        return ResponseEntity.ok(timetable)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        timetableService.delete(
            id = id,
            user = user,
        )
        return ResponseEntity.noContent().build()
    }
}
