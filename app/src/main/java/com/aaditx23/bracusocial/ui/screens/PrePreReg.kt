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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun PrePreReg(
    courseList: MutableList<JSONObject>,
    selectedCourseList: MutableList<JSONObject>,
    addCourse: (course: JSONObject) -> Unit,
    removeCourse: (course: JSONObject) -> Unit

){
    AllCourseList(courseList = courseList, addCourse = addCourse)
    SelectedCourses(courseList = selectedCourseList, removeCourse = removeCourse)



}


@Composable
fun AllCourseList(courseList: MutableList<JSONObject>, addCourse: (course: JSONObject) -> Unit){
    Box(
        modifier = Modifier
            .size(height = 330.dp, width = 150.dp)
            .padding(top = 100.dp)
    ) {
        LazyColumn {
            items(courseList.size){index ->
                val course = courseList[index]
                Course(course = course, courseAction = addCourse, left = true)
            }
        }
    }
}

@Composable
fun SelectedCourses(courseList: MutableList<JSONObject>, removeCourse: (course: JSONObject) -> Unit){
    Box(
        modifier = Modifier
            .size(height = 330.dp, width = 450.dp)
            .padding(top = 100.dp, start = 250.dp)
    ) {
        LazyColumn {
            items(courseList.size){index ->
                val course = courseList[index]
//                println(course.getString("Course"))
                Course(course = course, courseAction = removeCourse)
            }
        }
    }
}

@Composable
fun Course(course: JSONObject, courseAction: (course: JSONObject) -> Unit, left: Boolean = false){
    val text = "${course.getString("Course")} - ${course.getString("Section")}"
    var color = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    if (left && course.getBoolean("Selected")){
        color = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
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