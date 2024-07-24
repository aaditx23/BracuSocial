package com.aaditx23.bracusocial.ui.screens.PrePreReg

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.components.Routine

@Composable
fun MakeRoutine(
    coursevm : CourseVM,
    courseList: List<Course>,
    selectedCourseList: MutableList<Course>,
    selectedMap: MutableMap<String, Boolean>,
    addCourse: (course: Course) -> Unit,
    removeCourse: (course: Course) -> Unit,
    clearRoutine: () -> Unit

){
    val context = LocalContext.current
    //All Courses
    Row(
        modifier = Modifier
            .padding(top = 100.dp)
    ){
        Box(
            modifier = Modifier
                .size(height = 230.dp, width = 150.dp)
                .padding(start = 5.dp)
                .border(1.dp, Color.Magenta)
        ) {
            LazyColumn {
                items(courseList) { course ->
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
                .size(height = 230.dp, width = 150.dp)
                .padding(start = 5.dp)
                .border(1.dp, Color.Red)
        ) {
            LazyColumn {
                items(selectedCourseList) { course ->
                    CourseCard(
                        course = course,
                        courseAction = removeCourse
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 5.dp)
        ) {
            Button(onClick =
                {
                    if(selectedCourseList.isEmpty()){
                        Toast.makeText(context, "Empty routine cannot be saved", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        var savedRoutine = ""
                        var sortedList = mutableListOf<String>()
                        selectedCourseList.forEachIndexed { _, course ->
                            sortedList.add("${course.courseName} ${course.section}")
                        }
                        sortedList.sort()
                        sortedList.forEachIndexed { i, string ->
                            if (i == 0) {
                                savedRoutine = string
                            } else {
                                savedRoutine = "$savedRoutine,$string"
                            }
                        }
                        coursevm.addSavedRoutine(savedRoutine)
                        Toast.makeText(context, "Routine Saved", Toast.LENGTH_SHORT).show()
                        clearRoutine()
                    }
                }
            ) {
                Text(text = "Save")
            }
            Button(onClick = { clearRoutine() }) {
                Text(text = "Clear")
            }
        }
    }

    Box(
        modifier = Modifier
        ){
        Routine(courseList = selectedCourseList)
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
                defaultElevation = 5.dp,
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