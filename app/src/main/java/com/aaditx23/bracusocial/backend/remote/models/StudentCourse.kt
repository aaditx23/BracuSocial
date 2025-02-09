package com.aaditx23.bracusocial.backend.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StudentCourse(
    val courseCode: String,
    val sectionName: String,
    val sectionSchedule: SectionSchedule,
    val faculties: String,
    val roomNumber: String
)

@JsonClass(generateAdapter = true)
data class SectionSchedule(
    val classSchedules: List<ClassSchedule>,
    val midExamDate: String?,
    val midExamStartTime: String?,
    val midExamEndTime: String?,
    val finalExamDate: String?,
    val finalExamStartTime: String?,
    val finalExamEndTime: String?
)


@JsonClass(generateAdapter = true)
data class ClassSchedule(
    val startTime: String,
    val endTime: String,
    val day: String
)

@JsonClass(generateAdapter = true)
data class Exam(
    val date: String?,
    val startTime: String?,
    val endTime: String?
)
