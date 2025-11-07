package com.wafflestudio.spring2025.timetableLecture

import com.wafflestudio.spring2025.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class TimetableLectureException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)
// lectureId로 lecture를 찾을 수 없을 때
class LectureNotFoundException : TimetableLectureException(
    errorCode = 4001,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "해당 강의(lecture)를 찾을 수 없습니다.",
)
// 이미 timetableId + lectureId 조합이 존재할 때
class AlreadyExistsException : TimetableLectureException(
    errorCode = 4003,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "이미 해당 시간표에 같은 강의가 존재합니다.",
)

// DB 저장 중 예기치 못한 오류가 발생했을 때
class CreateFailedException(cause: Throwable? = null) : TimetableLectureException(
    errorCode = 4500,
    httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR,
    msg = "시간표-강의 추가에 실패했습니다.",
    cause = cause,
)

// 테스트용 강제 예외 (optional)
class TestException : TimetableLectureException(
    errorCode = 4999,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "테스트 중 강제 예외가 발생했습니다.",
)
class TimetableLectureNotFoundException : TimetableLectureException(
    errorCode = 4003,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "해당 시간표에 연결된 강의(timetableLecture)를 찾을 수 없습니다.",
)
class ForbiddenException : TimetableLectureException(
    errorCode = 4005,
    httpStatusCode = HttpStatus.FORBIDDEN,
    msg = "현재 사용자에게 해당 시간표를 수정하거나 삭제할 권한이 없습니다.",
)

