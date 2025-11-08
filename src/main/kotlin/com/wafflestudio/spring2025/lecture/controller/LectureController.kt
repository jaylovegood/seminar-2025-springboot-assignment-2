package com.wafflestudio.spring2025.lecture.controller

import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.dto.LecturePagingResponse
import com.wafflestudio.spring2025.lecture.service.LectureService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Lecture", description = "강의 검색 API")
@RestController
@RequestMapping("/api/v1/lectures")
class LectureController(
    private val lectureService: LectureService,
) {
    @GetMapping
    @Operation(summary = "강의 검색", description = "키워드로 강의를 검색합니다. 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "강의 검색 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 파라미터"),
        ],
    )
    fun search(
        @Parameter(description = "검색 키워드 (강의 제목, 강의 부제, 교수명 내 포함)")
        @RequestParam keyword: String,
        @Parameter(description = "학년도")
        @RequestParam year: Int,
        @Parameter(description = "학기(SPRING, SUMMER, FALL, WINTER)")
        @RequestParam semester: String,
        @Parameter(description = "다음 페이지의 시작 강의 ID (첫 페이지는 null)")
        @RequestParam(required = false) nextId: Long? = null,
        @Parameter(description = "페이지당 강의 개수 (기본값: 20)")
        @RequestParam(defaultValue = "20") limit: Int,
    ): ResponseEntity<LecturePagingResponse> {
        val result =
            lectureService.pageByKeyword(
                keyword = keyword,
                nextId = nextId,
                limit = limit,
                semester = Semester.valueOf(semester),
                academicYear = year,
            )
        return ResponseEntity.ok(result)
    }
}
