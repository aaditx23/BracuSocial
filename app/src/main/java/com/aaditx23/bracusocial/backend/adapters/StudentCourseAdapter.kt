package com.aaditx23.bracusocial.backend.adapters

import com.aaditx23.bracusocial.backend.remote.models.StudentCourse
import com.aaditx23.bracusocial.backend.remote.models.SectionSchedule
import com.aaditx23.bracusocial.backend.remote.models.ClassSchedule
import com.aaditx23.bracusocial.backend.remote.models.Exam
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.json.JSONObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

class StudentCourseAdapter {

    // FromJson adapter to convert from JSON to StudentCourse
    @FromJson
    fun fromJson(json: Map<String, Any?>): StudentCourse {
        println("Type of sectionSchedule: ${json["sectionSchedule"]?.javaClass?.simpleName}")

        // Get the sectionSchedule as a string, then parse it
        val sectionScheduleString = json["sectionSchedule"]?.toString()

        return StudentCourse(
            courseCode = json["courseCode"] as? String ?: "",
            sectionName = json["sectionName"] as? String ?: "",
            sectionSchedule = parseSectionSchedule(sectionScheduleString), // Parse the stringified JSON
            faculties = json["faculties"] as? String ?: "",
            roomNumber = json["roomNumber"] as? String ?: ""
        )
    }

    // Parse sectionSchedule string into SectionSchedule
    private fun parseSectionSchedule(sectionString: String?): SectionSchedule {
        // Check if the section string is null or empty
        if (sectionString.isNullOrEmpty()) {
            return SectionSchedule(emptyList(), "", "", "", "", "", "")
        }

        // Log the raw JSON string for debugging
        println("Parsing sectionSchedule JSON: $sectionString")

        // Use Moshi to parse the section schedule string (which is a JSON string)
        val moshi = Moshi.Builder().build()
        val sectionScheduleAdapter: JsonAdapter<SectionSchedule> = moshi.adapter(SectionSchedule::class.java)

        // Convert the string into a JSON object and then into a SectionSchedule object
        val jsonStringForMoshi = JSONObject(sectionString).toString()

        // Log the parsed JSON string for debugging
        println("JSON string for Moshi: $jsonStringForMoshi")

        // Parse the section schedule into SectionSchedule object
        val parsedSectionSchedule = sectionScheduleAdapter.fromJson(jsonStringForMoshi)

        // Log parsed sectionSchedule to check its content
        println("Parsed SectionSchedule: $parsedSectionSchedule")

        return parsedSectionSchedule ?: SectionSchedule(emptyList(), "", "", "", "", "", "")
    }

    // ToJson adapter to convert StudentCourse to JSON
//    @ToJson
//    fun toJson(studentCourse: StudentCourse): Map<String, Any?> {
//        return mapOf(
//            "courseCode" to studentCourse.courseCode,
//            "sectionName" to studentCourse.sectionName,
//            "sectionSchedule" to mapOf(
//                "classSchedules" to studentCourse.sectionSchedule.classSchedules.map { classSchedule ->
//                    mapOf(
//                        "startTime" to classSchedule.startTime,
//                        "endTime" to classSchedule.endTime,
//                        "day" to classSchedule.day
//                    )
//                },
//                "midExamDate" to studentCourse.sectionSchedule.midExam?.date,
//                "midExamStartTime" to studentCourse.sectionSchedule.midExam?.startTime,
//                "midExamEndTime" to studentCourse.sectionSchedule.midExam?.endTime,
//                "finalExamDate" to studentCourse.sectionSchedule.finalExam?.date,
//                "finalExamStartTime" to studentCourse.sectionSchedule.finalExam?.startTime,
//                "finalExamEndTime" to studentCourse.sectionSchedule.finalExam?.endTime
//            ),
//            "faculties" to studentCourse.faculties,
//            "roomNumber" to studentCourse.roomNumber
//        )
//    }
}
