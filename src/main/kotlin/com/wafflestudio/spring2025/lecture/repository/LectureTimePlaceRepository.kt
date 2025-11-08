package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListCrudRepository

interface LectureTimePlaceRepository : ListCrudRepository<LectureTimePlace, Long>
