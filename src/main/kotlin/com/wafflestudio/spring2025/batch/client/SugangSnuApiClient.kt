package com.wafflestudio.spring2025.batch.client

import org.springframework.web.reactive.function.client.WebClient

class SugangSnuApiClient(
    webClient: WebClient,
) : WebClient by webClient