package com.aaditx23.bracusocial.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.backend.local.models.Course
import org.json.JSONObject

@Composable
fun CourseItemJson(courseJson: JSONObject) {
    val courseName = courseJson.optString("Course", "N/A")
    val section = courseJson.optString("Section", "N/A")
    val classTime = courseJson.optString("ClassTime", "N/A")
    val classDays = courseJson.optJSONArray("ClassDay")?.let { jsonArray ->
        (0 until jsonArray.length()).joinToString(" ") { index ->
            jsonArray.optString(index)
        }
    } ?: "N/A"
    val classRoom = courseJson.optString("ClassRoom", "N/A")

    val hasLab = courseJson.optBoolean("Lab", false)
    val labDays = if (hasLab) courseJson.optJSONArray("LabDay")?.join(", ") ?: "N/A" else null
    val labTime = if (hasLab) {
        courseJson.optJSONArray("LabTime")?.let { jsonArray ->
            (0 until jsonArray.length()).joinToString(" ") { index ->
                jsonArray.optString(index)
            }
        } ?: courseJson.optString("LabTime", "N/A")
    } else null

    val labRoom = if (hasLab) courseJson.optString("LabRoom", "N/A") else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        Text(text = "Course: $courseName")
        Text(text = "Section: $section")
        Text(text = "Class Time: $classTime")
        Text(text = "Class Days: $classDays")
        Text(text = "Class Room: $classRoom")

        if (hasLab) {
            Text(text = "Lab Days: $labDays")
            Text(text = "Lab Time: $labTime")
            Text(text = "Lab Room: $labRoom")
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        Text(text = "Course: ${course.courseName}")
        Text(text = "Section: ${course.section}")
        Text(text = "Class Time: ${course.classTime}")
        Text(text = "Class Day: ${course.classDay}")
        Text(text = "Class Room: ${course.classRoom}")

        if (course.labDay != "-") {
            Text(text = "Lab Day: ${course.labDay}")
            Text(text = "Lab Time: ${course.labTime}")
            Text(text = "Lab Time: ${course.labRoom}")
        }

    }
}