package com.aaditx23.bracusocial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.local.viewmodels.SessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CourseHandler(){
    val sessionvm: SessionVM = hiltViewModel()
    val courseVM: CourseVM = hiltViewModel()
    
    val session by sessionvm.firstSession.collectAsState(initial = null)
    val allCourses by courseVM.allCourses.collectAsState()

    if (session == null){
        LaunchedEffect(Unit) {
            sessionvm.createSession()
        }
    }
    else{
        Column(modifier = Modifier.padding(top = 100.dp)) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    courseVM.createCourseDB(
                        courseName = "New course",
                        section = "01",
                        classDay = "Sun-Mon",
                        classTime = "05:00-06:20",
                        classRoom = "9A-2C",
                        labDay = "0",
                        labRoom = "0",
                        labTime = "0"
                    )
                }
            }) {
                Text(text = "Create Course")
            }
            allCourses.forEachIndexed { _, course ->
                Text(text = course._id.toString())
                Text(text = course.courseName)
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        courseVM.deleteCourse(course._id)
                    }
                }) {
                    Text(text = "Delete Course")
                }
            }
        }
    }
}