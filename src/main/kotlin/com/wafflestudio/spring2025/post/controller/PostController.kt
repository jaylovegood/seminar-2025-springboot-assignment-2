package com.wafflestudio.spring2025.post.controller

import com.wafflestudio.spring2025.post.dto.CreatePostRequest
import com.wafflestudio.spring2025.post.dto.CreatePostResponse
import com.wafflestudio.spring2025.post.dto.PostPagingResponse
import com.wafflestudio.spring2025.post.dto.UpdatePostRequest
import com.wafflestudio.spring2025.post.dto.UpdatePostResponse
import com.wafflestudio.spring2025.post.dto.core.PostDto
import com.wafflestudio.spring2025.post.service.PostService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@Tag(name = "Post", description = "게시물 관리 API")
@RestController
class PostController(
    private val postService: PostService,
) {
    @PostMapping("/api/v1/boards/{boardId}/posts")
    @Operation(summary = "게시물 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 생성 성공"),
            ApiResponse(responseCode = "400", description = "게시물 제목 또는 내용 공백"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시판"),
        ],
    )
    fun create(
        @Parameter(hidden = true) @LoggedInUser user: User,
        @PathVariable boardId: Long,
        @RequestBody createRequest: CreatePostRequest,
    ): ResponseEntity<CreatePostResponse> {
        val postDto =
            postService.create(
                title = createRequest.title,
                content = createRequest.content,
                user = user,
                boardId = boardId,
            )
        return ResponseEntity.ok(postDto)
    }

    @GetMapping("/api/v1/boards/{boardId}/posts")
    @Operation(summary = "게시물 목록 조회", description = "한 번에 가져올 게시물 개수를 지정하여\n 특정 게시판의 게시글을 페이지네이션 방식으로 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 목록 조회 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시판"),
        ],
    )
    fun page(
        @PathVariable boardId: Long,
        @RequestParam(value = "nextCreatedAt", required = false) nextCreatedAt: Long?,
        @RequestParam(value = "nextId", required = false) nextId: Long?,
        @RequestParam(value = "limit", defaultValue = "10") limit: Int,
    ): ResponseEntity<PostPagingResponse> {
        val postPagingResponse =
            postService.pageByBoardId(
                boardId,
                nextCreatedAt?.let { Instant.ofEpochMilli(it) },
                nextId,
                limit,
            )
        return ResponseEntity.ok(postPagingResponse)
    }

    @GetMapping("/api/v1/posts/{id}")
    @Operation(summary = "게시물 단건 조회", description = "게시물 아이디로 특정 게시물을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물"),
        ],
    )
    fun get(
        @PathVariable id: Long,
    ): ResponseEntity<PostDto> {
        val postDto = postService.get(id)
        return ResponseEntity.ok(postDto)
    }

    @PatchMapping("/api/v1/posts/{id}")
    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            ApiResponse(responseCode = "400", description = "게시물 제목 또는 내용 공백"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물"),
            ApiResponse(responseCode = "403", description = "수정할 수 없는 게시물"),
        ],
    )
    fun update(
        @PathVariable id: Long,
        @LoggedInUser user: User,
        @RequestBody updateRequest: UpdatePostRequest,
    ): ResponseEntity<UpdatePostResponse> {
        val postDto =
            postService.update(
                postId = id,
                title = updateRequest.title,
                content = updateRequest.content,
                user = user,
            )
        return ResponseEntity.ok(postDto)
    }

    @DeleteMapping("/api/v1/posts/{id}")
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "게시물 삭제 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물"),
            ApiResponse(responseCode = "403", description = "수정할 수 없는 게시물"),
        ],
    )
    fun delete(
        @PathVariable id: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        postService.delete(id, user)
        return ResponseEntity.noContent().build()
    }
}
