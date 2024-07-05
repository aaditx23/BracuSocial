package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.viewmodels.CourseVM
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun PrePreReg(
    selectedCourseList: MutableList<Course>,
    selectedMap: MutableMap<String, Boolean>,
    addCourse: (course: Course) -> Unit,
    removeCourse: (course: Course) -> Unit

){
    val coursevm : CourseVM = hiltViewModel()
    val courseList by coursevm.allCourses.collectAsState()
    //All Courses
    println(courseList.size)
    Box(
        modifier = Modifier
            .size(height = 330.dp, width = 150.dp)
            .padding(top = 100.dp)
    ) {
        LazyColumn {
            items(courseList.size){index ->
                val course = courseList[index]
                CourseCard(
                    course = course,
                    selectedMap = selectedMap,
                    courseAction = addCourse,
                    left = true
                )
            }
        }
    }

    //Selected Courses
    Box(
        modifier = Modifier
            .size(height = 330.dp, width = 450.dp)
            .padding(top = 100.dp, start = 250.dp)
    ) {
        LazyColumn {
            items(selectedCourseList.size){index ->
                val course = courseList[index]
                CourseCard(
                    course = course,
                    courseAction = removeCourse
                )
            }
        }
    }


}

@Composable
fun CourseCard(
    course: Course,
    selectedMap: MutableMap<String, Boolean> = mutableMapOf<String, Boolean>(),
    courseAction: (course: Course) -> Unit,
    left: Boolean = false
    ){
    val text = "${course.courseName} - ${course.section}"
    var color = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    if (left){
        if (selectedMap[course.courseName] == true){
            color = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        ElevatedCard(
            onClick = {
                      courseAction(course)
                      },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp,
            ),
            colors = color,
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
    
}