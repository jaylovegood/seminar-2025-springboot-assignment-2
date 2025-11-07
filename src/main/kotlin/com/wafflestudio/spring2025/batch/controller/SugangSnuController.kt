package com.wafflestudio.spring2025.batch.controller

import com.wafflestudio.spring2025.batch.service.SugangSnuService
import com.wafflestudio.spring2025.common.Semester
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Batch", description = "수강신청 사이트 크롤링 API")
@RestController
@RequestMapping("/api/v1/admin/batch")
class SugangSnuController(
    private val sugangSnuService: SugangSnuService,
) {
    @PostMapping("/sugang-snu")
    @Operation(summary = "SNU 수강신청 데이터 수집 및 저장", description = "SNU 수강신청 사이트에서 강의 데이터를 수집하여 데이터베이스에 저장합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "데이터 수집 및 저장 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 파라미터"),
            ApiResponse(responseCode = "500", description = "데이터 수집 실패"),
        ],
    )
    suspend fun fetchAndSaveSugangSnuLectures(
        @Parameter(description = "학년도 (예: 2025)")
        @RequestParam year: Int,
        @Parameter(description = "학기 (SPRING, SUMMER, FALL, WINTER)")
        @RequestParam semester: Semester,
    ): ResponseEntity<String> {
        sugangSnuService.getAndSaveSugangSnuLectures(year, semester)
        return ResponseEntity.ok("lectures fetched and saved successfully for $year-$semester")
    }
}