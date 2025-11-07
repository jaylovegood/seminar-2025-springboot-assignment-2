package com.wafflestudio.spring2025.timetableLecture

import com.wafflestudio.spring2025.DomainException
import org.springframework.http.HttpStatusCode

sealed class TimetableLectureException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class TimetableLectureTestException(
    msg: String = "This is a test exception for TimetableLecture.",
) : TimetableLectureException(
    errorCode = 9999,
    httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR,
    msg = msg,
)