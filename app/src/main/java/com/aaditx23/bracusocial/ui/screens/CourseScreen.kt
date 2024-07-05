package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CourseScreen() {
    val coursevm : CourseVM = hiltViewModel()
    val courseList by coursevm.allCourses.collectAsState()

    Box(modifier = Modifier.padding(top = 60.dp)){
        CourseList(courses = courseList)
    }

}

@Composable
fun CourseList(courses: List<Course>) {
    if (courses == null) return

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(courses.size) { index ->
            val course = courses[index]
            CourseItem(course)
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    println()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Text(text = "Course: ${course.courseName}")
        Text(text = "Section: ${course.section}")
        Text(text = "Class Time: ${course.classTime}")
        Text(text = "Class Day: ${course.classDay}")
        Text(text = "Class Room: ${course.classRoom}")

        if (course.labDay != "-") {
            Text(text = "Lab Time: ${course.labDay}")
            Text(text = "Lab Time: ${course.labTime}")
            Text(text = "Lab Time: ${course.labRoom}")
        }
    }
}


