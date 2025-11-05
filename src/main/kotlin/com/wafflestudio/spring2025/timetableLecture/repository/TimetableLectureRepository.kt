package com.wafflestudio.spring2025.timetableLecture.repository

import com.wafflestudio.spring2025.timetableLecture.model.TimetableLecture
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TimetableLectureRepository : CrudRepository<TimetableLecture, Long>{
    fun findByTimetableId(timetableId: Long): List<TimetableLecture>

    fun existsTimetableLecture(id: Long): Boolean


}
