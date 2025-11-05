package com.wafflestudio.spring2025.board.controller

import com.wafflestudio.spring2025.board.dto.CreateBoardRequest
import com.wafflestudio.spring2025.board.dto.CreateBoardResponse
import com.wafflestudio.spring2025.board.dto.ListBoardResponse
import com.wafflestudio.spring2025.board.service.BoardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Board", description = "게시판 관리 API")
@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService,
) {
    @PostMapping
    @Operation(summary = "게시판 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시판 생성"),
            ApiResponse(responseCode = "400", description = "게시판 이름 공백"),
            ApiResponse(responseCode = "409", description = "중복된 게시판 이름"),
        ],
    )
    fun create(
        @RequestBody createRequest: CreateBoardRequest,
    ): ResponseEntity<CreateBoardResponse> {
        val board = boardService.create(createRequest.name)
        return ResponseEntity.ok(board)
    }

    @GetMapping
    @Operation(summary = "게시판 목록", description = "존재하는 모든 게시판을 불러옵니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시판 목록 불러오기 성공"),
        ],
    )
    fun list(): ResponseEntity<ListBoardResponse> {
        val boards = boardService.list()
        return ResponseEntity.ok(boards)
    }
}
