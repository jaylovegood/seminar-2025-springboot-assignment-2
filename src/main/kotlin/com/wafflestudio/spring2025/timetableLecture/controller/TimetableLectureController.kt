package com.wafflestudio.spring2025.timetableLecture.controller

import com.wafflestudio.spring2025.timetableLecture.dto.CreateTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.dto.GetTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.dto.ListTimetableLectureResponse
import com.wafflestudio.spring2025.timetableLecture.repository.TimetableLectureRepository
import com.wafflestudio.spring2025.timetableLecture.service.TimetableLectureService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
    @Operation(summary = "시간표에 강의 추가")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "강의가 시간표에 성공적으로 추가됨"),
            ApiResponse(responseCode = "403", description = "다른 사용자의 시간표 접근 시"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 강의 또는 시간표"),
            ApiResponse(responseCode = "409", description = "이미 추가된 강의 또는 시간이 겹치는 강의"),
        ],
    )
    fun update(
        @PathVariable timetableId: Long,
        @PathVariable lectureId: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<CreateTimetableLectureResponse> {
        val tldto = timetableLectureService.create(lectureId, timetableId, user)
        return ResponseEntity.ok(tldto)
    }

    @Operation(summary = "시간표에서 강의 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "강의가 시간표에서 성공적으로 삭제됨"),
            ApiResponse(responseCode = "403", description = "다른 사용자의 시간표에 접근할 수 없음"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표 강의 또는 시간표"),
        ],
    )
    @DeleteMapping("/{timetableLectureId}")
    fun delete(
        @PathVariable timetableLectureId: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        timetableLectureService.delete(timetableLectureId, user)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "시간표 상세 조회 (강의 목록 + 총 학점)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시간표의 강의 목록과 총 학점 반환"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표"),
        ],
    )
    @GetMapping
    fun get(
        @PathVariable timetableId: Long,
    ): ResponseEntity<ListTimetableLectureResponse> = ResponseEntity.ok(timetableLectureService.get(timetableId))

    @Operation(summary = "시간표 내 특정 강의 상세 조회")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "강의 상세 정보 반환 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 시간표 강의 또는 강의"),
        ],
    )
    @GetMapping("/{timetableLectureId}")
    fun detail(
        @PathVariable timetableLectureId: Long,
    ): ResponseEntity<GetTimetableLectureResponse> = ResponseEntity.ok(timetableLectureService.detail(timetableLectureId))
}
