package com.wafflestudio.spring2025.lecture

import com.wafflestudio.spring2025.DomainException
import org.springframework.http.HttpStatusCode

sealed class LectureException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)
