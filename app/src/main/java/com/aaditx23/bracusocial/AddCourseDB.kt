package com.aaditx23.bracusocial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
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
    var status by rememberSaveable {
        mutableStateOf("Status")
    }

    if (session == null){
        LaunchedEffect(Unit) {
            sessionvm.createSession()
        }
    }
    else{
        Column(modifier = Modifier.padding(top = 100.dp)) {
            Text(text = status)
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    courseVM.populateDb{s ->
                        status = s
                    }
                    status = "Databse Created"
                }
            }) {
                Text(text = "Create DB")
            }
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    status = "Deleting all Entries"
                    courseVM.clearDB()
                    status = "Database empty"
                }

            }) {
                Text(text = "Delete All")
            }
            Button(onClick = {
                courseVM.refreshDB{s ->
                    status = s
                }
                status = "Database Refreshed"
            }) {
                Text(text = "Refresh DB")
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