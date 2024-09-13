package com.aaditx23.bracusocial.backend

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import org.json.JSONObject

fun filterCourseByNameSection(list: List<Course>, searchQuery: String): List<Course>{
    return list.filter { course ->
        val query = searchQuery.trim().split("-")
        when (query.size) {
            1 -> course.courseName.contains(query[0].trim(), ignoreCase = true)
            2 -> course.courseName.contains(query[0].trim(), ignoreCase = true) &&
                    course.section.contains(query[1].trim(), ignoreCase = true)
            else -> false
        }
    }
}

fun filterCourseByFaculty(list: List<Course>, searchQuery: String): List<Course> {
    return list.filter { course ->
        course.faculty.contains(searchQuery.trim(), ignoreCase = true)
    }
}

fun filterCourseByDays(list: List<Course>, searchQuery: String): List<Course> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { course ->
        val classDayMatches = course.classDay?.trim()?.contains(trimmedQuery, ignoreCase = true) == true
        val labDayMatches = course.labDay?.trim()?.contains(trimmedQuery, ignoreCase = true) == true

        classDayMatches || (course.labDay != null && course.labDay != "-" && labDayMatches)
    }
}


fun filterCourseByTimes(list: List<Course>, searchQuery: String): List<Course> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { course ->
        val classTimeMatches = course.classTime?.trim()?.contains(trimmedQuery, ignoreCase = true) == true
        val labTimeMatches = course.labTime?.trim()?.contains(trimmedQuery, ignoreCase = true) == true

        classTimeMatches || (course.labTime != null && course.labTime != "-" && labTimeMatches)
    }
}


fun filterCourseByRooms(list: List<Course>, searchQuery: String): List<Course> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { course ->
        val classRoomMatches = course.classRoom?.trim()?.contains(trimmedQuery, ignoreCase = true) == true
        val labRoomMatches = course.labRoom?.trim()?.contains(trimmedQuery, ignoreCase = true) == true

        classRoomMatches || (course.labRoom != null && course.labRoom != "-" && labRoomMatches)
    }
}

// JSON Course
fun filterCourseJsonByNameSection(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    return list.filter { courseJson ->
        val query = searchQuery.trim().split("-")
        val courseName = courseJson.optString("Course", "")
        val section = courseJson.optString("Section", "")

        when (query.size) {
            1 -> courseName.contains(query[0].trim(), ignoreCase = true)
            2 -> courseName.contains(query[0].trim(), ignoreCase = true) &&
                    section.contains(query[1].trim(), ignoreCase = true)
            else -> false
        }
    }
}


// Profiles

fun filterProfileByName(list: List<ProfileProxy>, searchQuery: String): List<ProfileProxy>{
    val trimmedQuery = searchQuery.trim()

    return list.filter { profile ->
        profile.studentName.trim().contains(trimmedQuery, ignoreCase = true)
    }
}
fun filterProfileByID(list: List<ProfileProxy>, searchQuery: String): List<ProfileProxy>{
    val trimmedQuery = searchQuery.trim()

    return list.filter { profile ->
        profile.studentId.trim().contains(trimmedQuery, ignoreCase = true)
    }
}
fun filterProfileByCourse(list: List<ProfileProxy>, searchQuery: String): List<ProfileProxy>{
    val trimmedQuery = searchQuery.trim()

    return list.filter { profile ->
        profile.enrolledCourses.trim().contains(trimmedQuery, ignoreCase = true)
    }
}