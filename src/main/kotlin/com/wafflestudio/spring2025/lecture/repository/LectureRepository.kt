package com.wafflestudio.spring2025.lecture.repository

import com.wafflestudio.spring2025.lecture.model.Lecture
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


interface LectureRepository : CrudRepository<Lecture, Long>{}
