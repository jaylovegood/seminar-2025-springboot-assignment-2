package com.wafflestudio.spring2025

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.helper.DataGenerator
import com.wafflestudio.spring2025.lecture.dto.LecturePagingResponse
import com.wafflestudio.spring2025.lecture.dto.core.LectureDto
import com.wafflestudio.spring2025.timetable.dto.CreateTimetableRequest
import com.wafflestudio.spring2025.timetable.dto.ListTimetableResponse
import com.wafflestudio.spring2025.timetable.dto.UpdateTimetableRequest
import com.wafflestudio.spring2025.timetable.repository.TimetableRepository
import org.junit.jupiter.api.Assertions.assertTrue
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
        private val timetableRepository: TimetableRepository,
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
            // 시간표 상세 정보를 조회할 수 있다
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

        @Test
        fun `should add a course to timetable`() {
            // 시간표에 강의를 추가할 수 있다
        }

        @Test
        fun `should return error when adding overlapping course to timetable`() {
            // 시간표에 강의 추가 시, 시간이 겹치면 에러를 반환한다
        }

        @Test
        fun `should not add a course to another user's timetable`() {
            // 다른 사람의 시간표에는 강의를 추가할 수 없다
        }

        @Test
        fun `should remove a course from timetable`() {
            // 시간표에서 강의를 삭제할 수 있다
        }

        @Test
        fun `should not remove a course from another user's timetable`() {
            // 다른 사람의 시간표에서는 강의를 삭제할 수 없다
        }

        //        @Disabled("곧 안내드리겠습니다")
        @Test
        fun `should fetch and save course information from SNU course registration site`() {
            // 서울대 수강신청 사이트에서 강의 정보를 가져와 저장할 수 있다
        }

        @Test
        fun `should return correct course list and total credits when retrieving timetable details`() {
            // 시간표 상세 조회 시, 강의 정보 목록과 총 학점이 올바르게 반환된다
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
