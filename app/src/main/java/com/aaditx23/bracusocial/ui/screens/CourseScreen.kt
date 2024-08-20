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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.SessionVM
import com.aaditx23.bracusocial.components.CloseButtonDialog
import com.aaditx23.bracusocial.components.FilterCourseList
import com.aaditx23.bracusocial.components.SearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CourseScreen(dbStatus: Boolean){
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
    var totalCourses by remember { mutableIntStateOf(0) }
    var addedCourses by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    fun refresh(){
        status = "Refreshing DB"
        courseVM.refreshDB(
            onSet = {s->
                status = s
            },
            status = {b->
                isLoading = b
            },
            setSize = {size ->
                totalCourses = size
            },
            setProgress = {progress ->
                addedCourses = progress
            }

        )
    }

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
        coroutineScope.launch {
            if (!dbStatus){
                refresh()
            }
        }

    }

    Column(modifier = Modifier.padding(top = 80.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    refresh()
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
            CloseButtonDialog(
                title = "Collecting courses",
                message = "Please wait\n$status",
                total = totalCourses,
                done = addedCourses
            )
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