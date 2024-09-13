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
fun filterJsonByNameSection(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    return list.filter { json ->
        val query = searchQuery.trim().split("-")
        when (query.size) {
            1 -> json.optString("Course", "").contains(query[0].trim(), ignoreCase = true)
            2 -> json.optString("Course", "").contains(query[0].trim(), ignoreCase = true) &&
                    json.optString("Section", "").contains(query[1].trim(), ignoreCase = true)
            else -> false
        }
    }
}

fun filterJsonByFaculty(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    return list.filter { json ->
        json.optString("Faculty", "").contains(searchQuery.trim(), ignoreCase = true)
    }
}

fun filterJsonByDays(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { json ->
        val classDays = json.optJSONArray("ClassDay")?.let { jsonArray ->
            (0 until jsonArray.length()).joinToString(" ") { index ->
                jsonArray.optString(index)
            }
        } ?: "N/A"
        val labDays = json.optJSONArray("ClassDay")?.let { jsonArray ->
            (0 until jsonArray.length()).joinToString(" ") { index ->
                jsonArray.optString(index)
            }
        } ?: "N/A"

        classDays.contains(trimmedQuery) || (labDays.contains(trimmedQuery))
    }
}

fun filterJsonByTimes(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { json ->
        val classTime = json.optString("ClassTime", "").contains(trimmedQuery, ignoreCase = true)
        val labTime = json.optString("LabTime", "").contains(trimmedQuery, ignoreCase = true)

        classTime || labTime
    }
}

fun filterJsonByRooms(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    val trimmedQuery = searchQuery.trim()

    return list.filter { json ->
        val classRoom = json.optString("ClassRoom", "").contains(trimmedQuery, ignoreCase = true)
        val labRoom = json.optString("LabRoom", "").contains(trimmedQuery, ignoreCase = true)

        classRoom || labRoom
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