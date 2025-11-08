package com.wafflestudio.spring2025.batch.service

import com.wafflestudio.spring2025.batch.model.SugangSnuLecture
import com.wafflestudio.spring2025.batch.repository.SugangSnuRepository
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.batch.mapping.SugangSnuMappings.convertSugangSnuRowToLecture
import com.wafflestudio.spring2025.batch.mapping.SugangSnuMappings.sugangSnuLectureToLectureModel
import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.lecture.model.Lecture
import com.wafflestudio.spring2025.lecture.model.LectureTimePlace
import com.wafflestudio.spring2025.lecture.repository.LectureRepository
import com.wafflestudio.spring2025.lecture.repository.LectureTimePlaceRepository
import org.springframework.stereotype.Service
import org.apache.poi.hssf.usermodel.HSSFWorkbook

@Service
class SugangSnuService (
    private val sugangSnuRepository: SugangSnuRepository,
    private val lectureRepository: LectureRepository,
    private val lectureTimePlaceRepository: LectureTimePlaceRepository,
) {

    suspend fun getAndSaveSugangSnuLectures(
        year: Int,
        semester: Semester,
    ): Int {
        val lectures = getSugangSnuLectures(year, semester)
        return saveSugangSnuLectures(lectures)
    }

    private suspend fun getSugangSnuLectures(
        year: Int,
        semester: Semester,
    ): List<SugangSnuLecture>{

        val lectureXlsx = sugangSnuRepository.getSugangSnuLecturesDataBuffer(
            year = year,
            semester = semester,
            language = "ko"
        )
        val sheet = HSSFWorkbook(lectureXlsx.asInputStream()).getSheetAt(0)
        sheet
        val columnNameIndex = sheet.toList()[2].associate { it.stringCellValue to it.columnIndex }

        return sheet
            .drop(3)
            .map { row ->
                convertSugangSnuRowToLecture(row.toList(), columnNameIndex, year, semester)
            }.also {
                lectureXlsx.release()
            }
    }

    private fun saveSugangSnuLectures(
        sugangSnuLectures: List<SugangSnuLecture>
    ): Int{
        val lectureIds: List<Long> = sugangSnuLectures
            .map { sugangSnuLectureToLectureModel(it) }
            .let { lectureRepository.saveAll(it) }
            .map { it.id!! }

        val lectureSchedules: List<List<LectureSchedule>> = sugangSnuLectures
            .map { it.schedule }

        lectureIds
            .zip(lectureSchedules)
            .map { (lectureId, lectureSchedules) ->
                lectureSchedules.map {
                    LectureTimePlace(
                        lectureId = lectureId,
                        id = null,
                        schedule = it,
                    )
                }
            }
            .flatten()
            .let { lectureTimePlaceRepository.saveAll(it) }
        return lectureIds.size
    }

}