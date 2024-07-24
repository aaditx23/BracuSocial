package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.SessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CourseScreen(){
    val sessionvm: SessionVM = hiltViewModel()
    val courseVM: CourseVM = hiltViewModel()

    val allCourses by courseVM.allCourses.collectAsState()
    val allSessions by sessionvm.allSessions.collectAsState()
    var status by rememberSaveable {
        mutableStateOf("Status")
    }
    var isSessionReady by rememberSaveable {
        mutableStateOf(false)
    }


    LaunchedEffect(allSessions) {
        println(allSessions)
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            isSessionReady = true
//            if(allSessions.isEmpty()){
//                sessionvm.createSession()
//            }
        }

    }
    if(isSessionReady){
        Column(modifier = Modifier.padding(top = 100.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(text = status)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        courseVM.populateDb{s ->
                            status = s
                        }
                        status = "Database Created"
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
                    status = "Refreshing DB"
                    courseVM.refreshDB{s ->
                        status = s
                    }
                    status = "Databsae Refreshed"

                }) {
                    Text(text = "Refresh DB")
                }
            }


        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 200.dp)
        ) {
            items(allCourses){course ->
                CourseItem(course = course)
            }
        }
        
    }
    else{
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Checking Session")
                CircularProgressIndicator()
            }
        }

    }

        
}

@Composable
fun CourseItem(course: Course) {
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
            Text(text = "Lab Day: ${course.labDay}")
            Text(text = "Lab Time: ${course.labTime}")
            Text(text = "Lab Time: ${course.labRoom}")
        }

    }
}