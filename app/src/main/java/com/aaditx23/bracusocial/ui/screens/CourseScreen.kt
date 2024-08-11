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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.SessionVM
import com.aaditx23.bracusocial.components.FilterCourseList
import com.aaditx23.bracusocial.components.SearchBar
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
        mutableStateOf("")
    }
    var isSessionReady by rememberSaveable {
        mutableStateOf(false)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourseList by remember { mutableStateOf(allCourses) }
    var isFiltering by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Filtering logic within a coroutine
    LaunchedEffect(searchQuery) {
        coroutineScope.launch {
            isFiltering = true
            delay(300) // Optional delay to simulate processing time
            filteredCourseList = FilterCourseList(list = allCourses, searchQuery = searchQuery.text)
            isFiltering = false

        }
    }


    LaunchedEffect(key1 = allSessions) {
        println(allSessions)
        coroutineScope.launch {
            delay(500)
            isSessionReady = true
//            if(allSessions.isEmpty()){
//                sessionvm.createSession()
//            }
        }

    }
    if(isSessionReady){

        Column(modifier = Modifier.padding(top = 80.dp)) {
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
                Button(
                    onClick = {
                        status = "Refreshing DB"
                        courseVM.refreshDB(
                            onSet = {s->
                                status = s
                            },
                            status = {b->
                                isLoading = b
                            }

                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Refresh DB")
                }
            }
            SearchBar(action = {query ->
                    searchQuery = query
                },
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = 40.dp,
                textSize = 16.sp
            )
            if (isFiltering){
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center

                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Searching Course ${searchQuery.text}")
                        CircularProgressIndicator()
                    }
                }
            }
            else if(isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center

                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Updating Database, please wait")
                        CircularProgressIndicator()
                    }
                }
            }
            else{
                LazyColumn() {
                    items(
                        if(searchQuery.text == ""){
                            allCourses
                        }
                        else{
                            filteredCourseList
                        }
                    ){course ->
                        CourseItem(course = course)
                    }
                }
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