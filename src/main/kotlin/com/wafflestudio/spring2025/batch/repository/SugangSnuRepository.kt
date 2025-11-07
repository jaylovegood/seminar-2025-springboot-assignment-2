package com.wafflestudio.spring2025.batch.repository

import com.wafflestudio.spring2025.batch.client.SugangSnuApiClient
import com.wafflestudio.spring2025.batch.mapping.SugangSnuMappings.semesterToSugangSnuSearchString
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import org.springframework.core.io.buffer.PooledDataBuffer
import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.createExceptionAndAwait

interface SugangSnuRepository : CrudRepository<Lecture, Long>, SugangSnuClientRepository

interface SugangSnuClientRepository {
    suspend fun getSugangSnuLecturesDataBuffer(
        year: Int,
        semester: Semester,
        language: String = "ko",
    ): PooledDataBuffer
}


// thanks (a lot) to SNUTT

@Repository
class SugangSnuClientRepositoryImpl(
    private val sugangSnuClient: SugangSnuApiClient,
) : SugangSnuClientRepository {

    companion object {
//        const val SUGANG_SNU_COURSEBOOK_PATH = "/sugang/cc/cc100ajax.action"
//        const val DEFAULT_COURSEBOOK_PARAMS = "openUpDeptCd=&openDeptCd="
//        const val SUGANG_SNU_SEARCH_PATH = "/sugang/cc/cc100InterfaceSrch.action"
//        const val DEFAULT_SEARCH_PAGE_PARAMS = "workType=S&sortKey=&sortOrder="
//        const val SUGANG_SNU_SEARCH_POPUP_PATH = "/sugang/cc/cc101ajax.action"
//        const val DEFAULT_SEARCH_POPUP_PARAMS = """t_profPersNo=&workType=+&sbjtSubhCd=000"""
        const val SUGANG_SNU_LECTURE_EXCEL_DOWNLOAD_PATH = "/sugang/cc/cc100InterfaceExcel.action"
        val DEFAULT_LECTURE_EXCEL_DOWNLOAD_PARAMS =
            """
            seeMore=더보기&
            srchBdNo=&srchCamp=&srchOpenSbjtFldCd=&srchCptnCorsFg=&
            srchCurrPage=1&
            srchExcept=&srchGenrlRemoteLtYn=&srchIsEngSbjt=&
            srchIsPendingCourse=&srchLsnProgType=&srchMrksApprMthdChgPosbYn=&srchMrksGvMthd=&
            srchOpenUpDeptCd=&srchOpenMjCd=&srchOpenPntMax=&srchOpenPntMin=&srchOpenSbjtDayNm=&
            srchOpenSbjtNm=&srchOpenSbjtTm=&srchOpenSbjtTmNm=&srchOpenShyr=&srchOpenSubmattCorsFg=&
            srchOpenSubmattFgCd1=&srchOpenSubmattFgCd2=&srchOpenSubmattFgCd3=&srchOpenSubmattFgCd4=&
            srchOpenSubmattFgCd5=&srchOpenSubmattFgCd6=&srchOpenSubmattFgCd7=&srchOpenSubmattFgCd8=&
            srchOpenSubmattFgCd9=&srchOpenDeptCd=&srchOpenUpSbjtFldCd=&
            srchPageSize=9999&
            srchProfNm=&srchSbjtCd=&srchSbjtNm=&srchTlsnAplyCapaCntMax=&srchTlsnAplyCapaCntMin=&srchTlsnRcntMax=&srchTlsnRcntMin=&
            workType=EX
            """.trimIndent().replace("\n", "")
    }

    override suspend fun getSugangSnuLecturesDataBuffer(
        year: Int,
        semester: Semester,
        language: String,
    ): PooledDataBuffer =
        sugangSnuClient
            .get()
            .uri { builder ->
                builder.run {
                    path(SUGANG_SNU_LECTURE_EXCEL_DOWNLOAD_PATH)
                    query(DEFAULT_LECTURE_EXCEL_DOWNLOAD_PARAMS)
                    queryParam("srchLanguage", language)
                    queryParam("srchOpenSchyy", year)
                    queryParam("srchOpenShtm", semesterToSugangSnuSearchString (semester))
                    build()
                }
            }.accept(MediaType.TEXT_HTML)
            .awaitExchange {
                if (it.statusCode().is2xxSuccessful) {
                    it.awaitBody()
                } else {
                    throw it.createExceptionAndAwait()
                }
            }
}