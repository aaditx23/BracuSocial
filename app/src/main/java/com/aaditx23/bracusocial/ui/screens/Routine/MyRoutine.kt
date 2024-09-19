package com.aaditx23.bracusocial.ui.screens.Routine

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.repositories.getToday
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import com.aaditx23.bracusocial.components.Day
import com.aaditx23.bracusocial.days
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val timeSlots = arrayOf(
    "08:00 AM-09:20 AM",
    "08:00 AM-10:50 AM", //lab
    "09:30 AM-10:50 AM",
    "11:00 AM-12:20 PM",
    "11:00 AM-01:50 PM", //lab
    "12:30 PM-01:50 PM",
    "02:00 PM-03:20 PM",
    "02:00 PM-04:50 PM", //lab
    "03:30 PM-04:50 PM",
    "05:00 PM-06:20 PM",
    "05:00 PM-08:00 PM", //lab
    "06:30 PM-08:00 PM"
)
//val days = listOf(
//    "Sa",
//    "Su",
//    "Mo",
//    "Tu",
//    "We",
//    "Th",
//    "Fr"
//)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyRoutine(routinevm: RoutineVM, navController: NavHostController, ){

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    val currentDay by rememberSaveable {
        mutableStateOf(getToday().slice(0..1))
    }
    var isLabToday by rememberSaveable {
        mutableStateOf(false)
    }
    var noCourses by rememberSaveable {
        mutableStateOf(false)
    }
    val map by rememberSaveable {
        mutableStateOf(mutableMapOf<String, MutableMap<String, String>>().apply {
            days.forEach { day ->
                this[day] = mutableMapOf<String, String>().apply {
                    timeSlots.forEach { time -> this[time] = "" }
                }
            }
        })
    }
    // {Day: {Time: Course} }
    val nonEmpty by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    fun add(entry: String){
        if (!nonEmpty.contains(entry)){
            nonEmpty.add(entry)
        }
    }



    var scope = rememberCoroutineScope()
    var listState = rememberLazyListState()
    var myCourses: List<Course> = listOf()
    routinevm.getMyCourses { list ->
        myCourses =  list
    }

    LaunchedEffect(myCourses) {
        scope.launch {
            delay(500)
            if(myCourses.isNotEmpty()){
                myCourses.forEachIndexed { _, course ->
                    val lab = course.labDay!!
                    if (lab == currentDay) isLabToday = true
                    if (lab != "-" ){
                        val temp = lab.split(" ")
                        temp.forEach { l ->
                            val time = course.labTime!!
                            val tempString = "${course.courseName},${course.section},${course.labRoom}"
                            map[l]?.set(time, tempString)
                            add(l)
                        }
                    }
                    val classDay = course.classDay!!
                    val temp = classDay.split(" ")
                    temp.forEach { c->
                        val time = course.classTime!!
                        val tempString = "${course.courseName},${course.section},${course.classRoom}"
                        map[c]?.set(time, tempString)
                        add(c)
                    }
                }
                nonEmpty.remove("")
                isLoading = false
                listState.animateScrollToItem(days.indexOf(currentDay))
            }
            else{
                isLoading = false
                noCourses = true
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
    else if (noCourses){

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "No courses added yet")
            Button(onClick = {
                navController.navigate("AddCourseFromProfile")
            }) {
                Text(text = "Add courses")
            }
        }

    }
    else{
//        println(nonEmpty.sorted())
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 130.dp),
            state = listState
        ) {
            items(days){ key ->
                if (nonEmpty.contains(key)){
                    Day(key, (map[key]!!), key == currentDay)
                }

            }
        }
    }


}



