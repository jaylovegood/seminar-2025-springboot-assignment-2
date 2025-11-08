package com.wafflestudio.spring2025.batch.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

// Thanks (a lot) to SNUTT

@Configuration
class SugangSnuApiConfig {
    companion object {
        const val BASEURL = "https://sugang.snu.ac.kr"
    }

    @Bean
    fun sugangSnuApiClient(): SugangSnuApiClient {
        val exchangeStrategies: ExchangeStrategies =
            ExchangeStrategies
                .builder()
                .codecs { it.defaultCodecs().maxInMemorySize(-1) }
                .build() // 용량 제한 해제

        return WebClient
            .builder()
            .baseUrl(BASEURL)
            .exchangeStrategies(exchangeStrategies)
            .defaultHeaders {
                it.setAll(
                    mapOf(
                        "User-Agent" to
                            """
                            Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)
                            AppleWebKit/537.36 (KHTML, like Gecko)
                            Chrome/86.0.4240.80
                            Safari/537.36
                            """.trimIndent().replace("\n", " "),
                        "Referer" to "https://sugang.snu.ac.kr/sugang/cc/cc100InterfaceExcel.action",
                    ),
                )
            }.build()
            .let(::SugangSnuApiClient)
    }
}
