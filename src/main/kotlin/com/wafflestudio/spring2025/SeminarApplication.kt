package com.wafflestudio.spring2025

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        SecurityAutoConfiguration::class,
    ],
)
class SeminarApplication

fun main(args: Array<String>) {
    runApplication<SeminarApplication>(*args)
}
