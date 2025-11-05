package com.wafflestudio.spring2025.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Spring 2025 Timetable API TEAM 4")
                    .version("v1")
                    .description("서울대 수강신청 관리용 프로젝트"),
            )
}
