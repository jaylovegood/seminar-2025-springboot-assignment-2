package com.wafflestudio.spring2025.comment.controller

import com.wafflestudio.spring2025.comment.dto.CreateCommentRequest
import com.wafflestudio.spring2025.comment.dto.CreateCommentResponse
import com.wafflestudio.spring2025.comment.dto.UpdateCommentRequest
import com.wafflestudio.spring2025.comment.dto.UpdateCommentResponse
import com.wafflestudio.spring2025.comment.dto.core.CommentDto
import com.wafflestudio.spring2025.comment.service.CommentService
import com.wafflestudio.spring2025.user.LoggedInUser
import com.wafflestudio.spring2025.user.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Comment", description = "댓글 관리 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
class CommentController(
    private val commentService: CommentService,
) {
    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "게시물에 달린 모든 댓글 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물"),
        ]
    )
    fun list(
        @PathVariable postId: Long,
    ): ResponseEntity<List<CommentDto>> {
        val comments = commentService.list(postId)
        return ResponseEntity.ok(comments)
    }

    @PostMapping
    @Operation(summary = "댓글 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
            ApiResponse(responseCode = "400", description = "댓글 공백"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물"),
        ]
    )
    fun create(
        @PathVariable postId: Long,
        @RequestBody createRequest: CreateCommentRequest,
        @LoggedInUser user: User,
    ): ResponseEntity<CreateCommentResponse> {
        val comment =
            commentService.create(
                postId = postId,
                content = createRequest.content,
                user = user,
            )
        return ResponseEntity.ok(comment)
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", description = "댓글의 내용을 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            ApiResponse(responseCode = "400", description = "댓글 공백"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 댓글"),
            ApiResponse(responseCode = "403", description = "수정할 수 없는 댓글")
        ]
    )
    fun update(
        @PathVariable postId: Long,
        @PathVariable id: Long,
        @LoggedInUser user: User,
        @RequestBody updateRequest: UpdateCommentRequest,
    ): ResponseEntity<UpdateCommentResponse> {
        val comment =
            commentService.update(
                commentId = id,
                postId = postId,
                content = updateRequest.content,
                user = user,
            )
        return ResponseEntity.ok(comment)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 댓글"),
            ApiResponse(responseCode = "403", description = "삭제할 수 없는 댓글")
        ]
    )
    fun delete(
        @PathVariable postId: Long,
        @PathVariable id: Long,
        @LoggedInUser user: User,
    ): ResponseEntity<Unit> {
        commentService.delete(
            commentId = id,
            postId = postId,
            user = user,
        )
        return ResponseEntity.noContent().build()
    }
}
