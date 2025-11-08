package com.wafflestudio.spring2025

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.helper.DataGenerator
import com.wafflestudio.spring2025.lecture.dto.LecturePagingResponse
import com.wafflestudio.spring2025.lecture.dto.core.LectureDto
import com.wafflestudio.spring2025.lecture.repository.LectureTimePlaceRepository
import com.wafflestudio.spring2025.timetable.dto.CreateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.ListTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableRequest
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import org.junit.jupiter.api.Assertions.assertTrue
import com.wafflestudio.spring2025.timetableLecture.dto.CreateTimetableLectureRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
class TimetableIntegrationTest
    @Autowired
    constructor(
        private val mvc: MockMvc,
        private val mapper: ObjectMapper,
        private val dataGenerator: DataGenerator,
    ) {
    @Test
    fun `should create a timetable`() {
        // 시간표를 생성할 수 있다
        val (user, token) = dataGenerator.generateUser()
        val timetableName = "timetableName1"
        val year = 2025
        val semester = Semester.SPRING

        val request = CreateTimetableRequest(timetableName, year, semester)
        mvc
            .perform(
                post("/api/v1/timetables")
                    .header("Authorization", "Bearer $token")
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.user.id").value(user.id!!))
            .andExpect(jsonPath("$.timetableName").value(request.timetableName))
            .andExpect(jsonPath("$.year").value(request.year))
            .andExpect(jsonPath("$.semester").value(request.semester.name))
    }

    @Test
    fun `should retrieve all own timetables`() {
        // 자신의 모든 시간표 목록을 조회할 수 있다
        val (user1, token1) = dataGenerator.generateUser()
        repeat(10) {
            dataGenerator.generateTimetable(user = user1)
        }

        val (user2, token2) = dataGenerator.generateUser()
        repeat(5) {
            dataGenerator.generateTimetable(user = user2)
        }

        val response =
            mvc
                .perform(
                    get("/api/v1/timetables")
                        .header("Authorization", "Bearer $token1")
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    mapper.readValue(it, object : TypeReference<ListTimetableResponse>() {})
                }
        assert(response.size == 10)
        assert(response.all { it.user.id == user1.id })
    }

    @Test
    fun `should retrieve timetable details`() {
        // 시간표를 조회할 수 있다.
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)
        val timetableId = timetable.id!!

        // when & then
        mvc
            .perform(
                get("/api/v1/timetables/{timetableId}/timetableLectures", timetableId)
                    .header("Authorization", "Bearer $token")
                    .accept(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$[0].timetableId").value(timetableId))
            .andExpect(jsonPath("$[0].lecture").exists())
            .andExpect(jsonPath("$[0].lecture.schedule").isArray)
    }

    @Test
    fun `should update timetable name`() {
        // 시간표 이름을 수정할 수 있다
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)
        val request = UpdateTimetableRequest("new name")

        mvc
            .perform(
                patch("/api/v1/timetables/${timetable.id!!}")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(timetable.id!!))
            .andExpect(jsonPath("$.user.id").value(user.id!!))
            .andExpect(jsonPath("$.timetableName").value(request.timetableName))
            .andExpect(jsonPath("$.year").value(timetable.year))
            .andExpect(jsonPath("$.semester").value(timetable.semester.name))
    }

    @Test
    fun `should not update another user's timetable`() {
        // 다른 사람의 시간표는 수정할 수 없다
        val timetable = dataGenerator.generateTimetable()
        val (_, otherToken) = dataGenerator.generateUser()
        val request = UpdateTimetableRequest("new name")

        mvc
            .perform(
                patch("/api/v1/timetables/${timetable.id!!}")
                    .header("Authorization", "Bearer $otherToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)),
            ).andExpect(status().isForbidden)
    }

    @Test
    fun `should delete a timetable`() {
        // 시간표를 삭제할 수 있다
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)

        mvc
            .perform(
                delete("/api/v1/timetables/${timetable.id!!}")
                    .header("Authorization", "Bearer $token"),
            ).andExpect(status().isNoContent)

        mvc
            .perform(
                get("/api/v1/timetables/${timetable.id!!}")
                    .header("Authorization", "Bearer $token"),
            ).andExpect(status().isNotFound)
    }

    @Test
    fun `should not delete another user's timetable`() {
        // 다른 사람의 시간표는 삭제할 수 없다
        val timetable = dataGenerator.generateTimetable()
        val (_, otherToken) = dataGenerator.generateUser()

        mvc
            .perform(
                delete("/api/v1/timetables/${timetable.id!!}")
                    .header("Authorization", "Bearer $otherToken"),
            ).andExpect(status().isForbidden)
    }


    @Test
    fun `should add a course to timetable`() {
        // 시간표에 강의를 추가할 수 있다.
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)
        val lecture = dataGenerator.generateLecture()
        val ltp = dataGenerator.generateLectureTimePlace(lecture.id!!)
        val request = CreateTimetableLectureRequest(timetable.id!!, lecture.id!!)

        mvc
            .perform(
                post("/api/v1/timetables/{timetableId}/timetableLectures/{lectureId}", timetable.id!!, lecture.id!!)
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.timetableId").value(timetable.id!!))
            .andExpect(jsonPath("$.lecture.id").value(lecture.id!!))
    }

    @Test
    fun `should return error when adding overlapping course to timetable`() {
        // 겹치는 강의는 추가할 수 없다
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)

        // 강의 A (기존)
        val lectureA = dataGenerator.generateLecture()
        dataGenerator.insertTimetableLecture(timetable, lectureA)
        val ltp = dataGenerator.generateLectureTimePlace(lectureA.id!!)
        // 강의 B (겹치는 시간으로)
        val lectureB = dataGenerator.generateLecture()
        dataGenerator.generateLectureTimePlacefix(lectureB.id!!, ltp.schedule)
        val request = CreateTimetableLectureRequest(timetable.id!!, lectureB.id!!)
        // when & then
        mvc
            .perform(
                post(
                    "/api/v1/timetables/{timetableId}/timetableLectures/{lectureId}",
                    timetable.id,
                    lectureB.id
                )
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should not add a course to another user's timetable`() {
        // 시간표 주인만 바꿀수 있다.
        // given
        val (owner, _) = dataGenerator.generateUser() // 시간표의 주인
        val (_, token) = dataGenerator.generateUser() // 다른 사용자
        val timetable = dataGenerator.generateTimetable(user = owner) // owner 소유의 시간표
        val lecture = dataGenerator.generateLecture()
        dataGenerator.generateLectureTimePlace(lecture.id!!)
        val request = CreateTimetableLectureRequest(timetable.id!!, lecture.id!!)

        // when & then
        mvc
            .perform(
                patch(
                    "/api/v1/timetables/{timetableId}/timetableLectures/{lectureId}",
                    timetable.id,
                    lecture.id!!,
                )
                    .header("Authorization", "Bearer $token") // attacker의 토큰으로 요청
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)),
            ).andExpect(status().isForbidden)
    }

    @Test
    fun `should remove a course from timetable`() {
        // 시간표를 지울 수 있다.
        // given
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)
        val lecture = dataGenerator.generateLecture()
        val timetableLecture = dataGenerator.insertTimetableLecture(timetable, lecture) // timetable_lecture 관계 추가

        // when & then
        mvc
            .perform(
                delete(
                    "/api/v1/timetables/{timetableId}/timetableLectures/{timetableLectureId}",
                    timetable.id,
                    timetableLecture.id,
                ).header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isNoContent)
        mvc
            .perform(
                delete(
                    "/api/v1/timetables/{timetableId}/timetableLectures/{timetableLectureId}",
                    timetable.id,
                    timetableLecture.id,
                ).header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isNotFound)
    }

    @Test
    fun `should not remove a course from another user's timetable`() {
        // 시간표 주인은 주인만 제거할 수 있다.
        // given
        val (owner, _) = dataGenerator.generateUser() // 시간표 주인
        val (_, token) = dataGenerator.generateUser() // 다른 사용자
        val timetable = dataGenerator.generateTimetable(user = owner)
        val lecture = dataGenerator.generateLecture()
        val ltp = dataGenerator.generateLectureTimePlace(lecture.id!!)
        val timetableLecture = dataGenerator.insertTimetableLecture(timetable, lecture)

        // when & then
        mvc
            .perform(
                delete(
                    "/api/v1/timetables/{timetableId}/timetableLectures/{timetableLectureId}",
                    timetable.id,
                    timetableLecture.id,
                ).header("Authorization", "Bearer $token") // 공격자 토큰
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isForbidden)

    }
    @Test
    fun `should return correct course list and total credits when retrieving timetable details`() {
        // 강의 상세 정보를 조회할 수 있다.
        val (user, token) = dataGenerator.generateUser()
        val timetable = dataGenerator.generateTimetable(user = user)
        val lecture = dataGenerator.generateLecture(title = "데이터베이스")
        val ltp = dataGenerator.generateLectureTimePlace(lecture.id!!)
        val timetableLecture = dataGenerator.insertTimetableLecture(timetable, lecture)

        // when & then
        mvc.perform(
            get(
                "/api/v1/timetables/{timetableId}/timetableLectures/{timetableLectureId}",
                timetable.id,
                timetableLecture.id
            )
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            // LectureDto가 정상적으로 내려오는지만 확인
            .andExpect(jsonPath("$.title").value("데이터베이스"))
    }
    @Test
    fun `should search for courses based on keyword with pagination`() {
        // 키워드로 강의를 검색할 수 있으며, 페이지네이션이 올바르게 동작한다
        repeat(500){
            dataGenerator.generateLecture()
        }
        val (_, token) = dataGenerator.generateUser()

        val response =
            mvc
                .perform(
                    get("/api/v1/lectures?keyword=title-3&limit=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $token")
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$.paging.hasNext").value(true))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    mapper.readValue(it, LecturePagingResponse::class.java)
                }
        assertKeywordIsInLectures("title-3", response.data)

        val nextResponse =
            mvc
                .perform(
                    get("/api/v1/lectures?keyword=title-3&nextId=${response.paging.nextId}&limit=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $token")
                ).andExpect(status().isOk)
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    mapper.readValue(it, LecturePagingResponse::class.java)
                }
        assertKeywordIsInLectures("title-3", nextResponse.data)
        assertTrue((response.data.map { it.id } + nextResponse.data.map { it.id }).toSet().size == 10)
        assertTrue(nextResponse.data.minOf { it.id } > response.paging.nextId!!)
    }

    private fun assertKeywordIsInLectures(
        keyword: String,
        lectures: List<LectureDto>
    ){
        lectures.forEach {
            assertTrue(
                it.title.contains(keyword)
                    .or(it.subtitle.contains(keyword))
                    .or(it.lecturer.contains(keyword))
            )
        }
    }



}