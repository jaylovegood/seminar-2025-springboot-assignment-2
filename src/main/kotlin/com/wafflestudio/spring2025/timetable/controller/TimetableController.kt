package com.wafflestudio.spring2025.timetable.controller

import com.wafflestudio.spring2025.timetable.dto.CreateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.CreateTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.core.TimetableDto
import com.wafflestudio.spring2025.timetable.service.TimetableService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Timetable", description = "시간표 관리 API")
@RestController
@RequestMapping("/api/v1/timetables")
class TimetableController(
    private val timetableService: TimetableService,
) {
    @PostMapping
    @Operation(summary = "시간표 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시간표 생성 성공"),
            ApiResponse(responseCode = "400", description = "시간표 이름 공백"),
            ApiResponse(responseCode = "409", description = "중복된 시간표 이름")
        ]
    )
    fun create(
        @RequestBody createRequest: CreateTimetableRequest,
        @Parameter(hidden = true) @LoggedInUser user: User,
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

    @Operation(summary = "시간표 수정", description = "시간표의 이름을 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시간표 수정 성공"),
            ApiResponse(responseCode = "400", description = "시간표 이름 공백"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표"),
            ApiResponse(responseCode = "403", description = "수정할 수 없는 시간표"),
        ]
    )
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Parameter(hidden = true) @LoggedInUser user: User,
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

    @Operation(summary = "시간표 목록", description = "사용자의 모든 시간표를 불러옵니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시간표 목록 불러오기 성공"),
        ]
    )
    @GetMapping
    fun list(
        @Parameter(hidden = true) @LoggedInUser user: User,
    ): ResponseEntity<List<TimetableDto>> {
        val timetables =
            timetableService.list(
                userId = user.id!!,
            )
        return ResponseEntity.ok(timetables)
    }

    @Operation(summary = "시간표 조회", description = "시간표 아이디로 시간표를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시간표 조회 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표"),
        ]
    )
    @GetMapping("/{id}")
    fun get(
        @Parameter(hidden = true) @LoggedInUser user: User,
        @PathVariable id: Long,
    ): ResponseEntity<TimetableDto> {
        val timetable =
            timetableService.get(
                user = user,
                id = id,
            )
        return ResponseEntity.ok(timetable)
    }

    @Operation(summary = "시간표 삭제", description = "시간표를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "시간표 삭제 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표"),
            ApiResponse(responseCode = "403", description = "삭제할 수 없는 시간표"),
        ]
    )
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @Parameter(hidden = true) @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        timetableService.delete(
            id = id,
            user = user,
        )
        return ResponseEntity.noContent().build()
    }
}
