package com.wafflestudio.spring2025.batch.mapping

import com.wafflestudio.spring2025.batch.model.SugangSnuLecture
import com.wafflestudio.spring2025.common.DayOfWeek
import com.wafflestudio.spring2025.common.LectureSchedule
import com.wafflestudio.spring2025.common.Semester
import com.wafflestudio.spring2025.lecture.model.Lecture
import org.apache.poi.ss.usermodel.Cell
import java.time.LocalTime

object SugangSnuMappings {
    private val classTimeRegEx =
        """^(?<day>[월화수목금토일])\((?<startHour>\d{2}):(?<startMinute>\d{2})~(?<endHour>\d{2}):(?<endMinute>\d{2})\)$""".toRegex()

    fun semesterToSugangSnuSearchString(semester: Semester): String =
        when (semester) {
            Semester.SPRING -> "U000200001U000300001"
            Semester.SUMMER -> "U000200001U000300002"
            Semester.FALL -> "U000200002U000300001"
            Semester.WINTER -> "U000200002U000300002"
        }

    fun koreanToDayOfWeek(kor: String): DayOfWeek =
        when (kor) {
            "월" -> DayOfWeek.MON
            "화" -> DayOfWeek.TUE
            "수" -> DayOfWeek.WED
            "목" -> DayOfWeek.THU
            "금" -> DayOfWeek.FRI
            "토" -> DayOfWeek.SAT
            "일" -> DayOfWeek.SUN
            else -> DayOfWeek.MON // FIXME
        }

    fun parseSugangSnuClassTime(timeString: String): LectureSchedule =
        classTimeRegEx.find(timeString)!!.groups.let { matchResult ->
            val dayOfWeek = matchResult["day"]!!.value
            val startHour = matchResult["startHour"]!!.value.toInt()
            val startMinute = matchResult["startMinute"]!!.value.toInt()
            val endHour = matchResult["endHour"]!!.value.toInt()
            val endMinute = matchResult["endMinute"]!!.value.toInt()
            LectureSchedule(
                dayOfWeek = koreanToDayOfWeek(dayOfWeek),
                startTime = LocalTime.of(startHour, startMinute),
                endTime = LocalTime.of(endHour, endMinute),
                location = "",
            )
        }

    fun convertTextToSchedules(
        classTimesTexts: List<String>,
        locationTexts: List<String>,
    ): List<LectureSchedule> =
        runCatching {
            val sugangSnuClassTimes: List<LectureSchedule> =
                classTimesTexts
                    .filter { it.isNotBlank() }
                    .map(::parseSugangSnuClassTime)
            val locationTexts =
                locationTexts.let { locationText ->
                    when (locationText.size) {
                        sugangSnuClassTimes.size -> locationText
                        1 -> List(sugangSnuClassTimes.size) { locationText.first() }
                        0 -> List(sugangSnuClassTimes.size) { "" }
                        else -> throw RuntimeException("locations does not match with times $classTimesTexts $locationTexts")
                    }
                }
            sugangSnuClassTimes
                .zip(locationTexts)
                .map { (classTime, location) ->
                    LectureSchedule(
                        dayOfWeek = classTime.dayOfWeek,
                        startTime = classTime.startTime,
                        endTime = classTime.endTime,
                        location = location,
                    )
                }.sortedWith(compareBy({ it.dayOfWeek }, { it.startTime }))
        }.getOrElse {
            emptyList()
        }

    fun convertSugangSnuRowToLecture(
        row: List<Cell>,
        columnNameIndex: Map<String, Int>,
        year: Int,
        semester: Semester,
    ): SugangSnuLecture {
        fun List<Cell>.getCellByColumnName(key: String): String =
            this[
                columnNameIndex.getOrElse(key) {
                    this.size
                },
            ].stringCellValue

        val classification = row.getCellByColumnName("교과구분").take(10)
        val college = row.getCellByColumnName("개설대학").take(30)
        val department = row.getCellByColumnName("개설학과").take(30)
        val academicCourse = row.getCellByColumnName("이수과정").take(20)
        val grade = row.getCellByColumnName("학년").ifEmpty { "0" }[0].code - '0'.code
        val courseNumber = row.getCellByColumnName("교과목번호").take(20)
        val lectureNumber = row.getCellByColumnName("강좌번호").take(20)
        val courseTitle = row.getCellByColumnName("교과목명").take(50)
        val courseSubtitle = row.getCellByColumnName("부제명").take(50)
        val credit = row.getCellByColumnName("학점").toInt()
        val classTimeText = row.getCellByColumnName("수업교시")
        val location = row.getCellByColumnName("강의실(동-호)(#연건, *평창)")
        val instructor = row.getCellByColumnName("주담당교수").take(50)

        val classTimes =
            convertTextToSchedules(classTimeText.split("/"), location.split("/"))

        val courseFullTitle = if (courseSubtitle.isEmpty()) courseTitle else "$courseTitle ($courseSubtitle)"

        return SugangSnuLecture(
            id = 0,
            lectureType = classification,
            department = department.replace("null", "").ifEmpty { college },
            target = academicCourse,
            courseNumber = courseNumber,
            lectureNumber = lectureNumber,
            title = courseFullTitle,
            credit = credit,
            lecturer = instructor,
            academicYear = year,
            semester = semester,
            schedule = classTimes,
            grade = grade,
            college = college,
            subtitle = courseSubtitle,
        )
    }

    fun sugangSnuLectureToLectureModel(sugangSnuLecture: SugangSnuLecture): Lecture =
        Lecture(
            id = null,
            academicYear = sugangSnuLecture.academicYear,
            semester = sugangSnuLecture.semester,
            lectureType = sugangSnuLecture.lectureType,
            college = sugangSnuLecture.college,
            department = sugangSnuLecture.department,
            target = sugangSnuLecture.target,
            grade = sugangSnuLecture.grade,
            courseNumber = sugangSnuLecture.courseNumber,
            lectureNumber = sugangSnuLecture.lectureNumber,
            title = sugangSnuLecture.title,
            subtitle = sugangSnuLecture.subtitle,
            credit = sugangSnuLecture.credit,
            lecturer = sugangSnuLecture.lecturer,
        )
}
