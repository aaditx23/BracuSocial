package com.aaditx23.bracusocial.ui.screens.Routine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyRoutine(routinevm: RoutineVM){
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var scope = rememberCoroutineScope()
    var myCourses: List<Course> = listOf()
    routinevm.getMyCourses { list ->
        myCourses =  list
    }
    LaunchedEffect(myCourses) {
        scope.launch {
            delay(200)
            if(myCourses.isNotEmpty()){
                isLoading = false
            }
        }
    }
    if(isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    else{
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp)
        ) {
            items(myCourses){ course ->
                Card {
                    Text(text = course.courseName)
                    Text(text = course.section)
                    Text(text = course.classTime!!)
                    Text(text = course.classDay!!)
                }
            }
        }
    }


}