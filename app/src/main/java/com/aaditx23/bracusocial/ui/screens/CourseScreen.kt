package com.aaditx23.bracusocial.ui.screens

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
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun CourseScreen(setJson: (courseList: JSONArray) -> Unit) {
    var courseList by remember { mutableStateOf<JSONArray?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val usisCrawler = UsisCrawler()
                val courses = usisCrawler.executeAsyncTask()
                courseList = courses
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.padding(top = 60.dp)){
        when {
            isLoading -> {
                // Show a loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                // Show an error message
                Text(text = "Failed to load data: $errorMessage")
            }
            courseList != null -> {
                // Show the list of courses
                setJson(courseList!!)
                CourseList(courses = courseList)
            }
        }
    }
}

@Composable
fun CourseList(courses: JSONArray?) {
    if (courses == null) return

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(courses.length()) { index ->
            val course = courses.getJSONObject(index)
            CourseItem(course)
        }
    }
}

@Composable
fun CourseItem(course: JSONObject) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Text(text = "Course: ${course.getString("Course")}")
        Text(text = "Section: ${course.getString("Section")}")
        Text(text = "Class Time: ${course.getString("ClassTime")}")
        if (course.has("ClassDay")) {
            Text(text = "Class Day: ${course.getJSONArray("ClassDay").join(", ")}")
        }
        if (course.has("LabTime")) {
            Text(text = "Lab Time: ${course.getString("LabTime")}")
        }
        if (course.has("LabDay")) {
            Text(text = "Lab Day: ${course.getJSONArray("LabDay").join(", ")}")
        }
    }
}


