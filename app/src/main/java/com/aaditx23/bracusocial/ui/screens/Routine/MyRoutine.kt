package com.aaditx23.bracusocial.ui.screens.Routine

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.repositories.getTimeSlot
import com.aaditx23.bracusocial.backend.local.repositories.getToday
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.paletteDarkGreen
import com.aaditx23.bracusocial.ui.theme.paletteDarkGreen2
import com.aaditx23.bracusocial.ui.theme.paletteGreen
import com.aaditx23.bracusocial.ui.theme.paletteLightPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val timeSlots = arrayOf(
    "08:00 AM - 09:20 AM",
    "08:00 AM - 10:50 AM", //lab
    "09:30 AM - 10:50 AM",
    "11:00 AM - 12:20 PM",
    "11:00 AM - 01:50 PM", //lab
    "12:30 PM - 01:50 PM",
    "02:00 PM - 03:20 PM",
    "02:00 PM - 04:50 PM", //lab
    "03:30 PM - 04:50 PM",
    "05:00 PM - 06:20 PM",
    "05:00 PM - 08:00 PM", //lab
    "06:30 PM - 08:00 PM"
)
val days = listOf(
    "Sa",
    "Su",
    "Mo",
    "Tu",
    "We",
    "Th",
    "Fr"
)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyRoutine(routinevm: RoutineVM){


    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var currentDay by rememberSaveable {
        mutableStateOf(getToday().slice(0..1))
    }
    var currentTimeSlot by rememberSaveable {
        mutableIntStateOf(-1)
    }
    var isLabToday by rememberSaveable {
        mutableStateOf(false)
    }
    var map by rememberSaveable {
        mutableStateOf(mutableMapOf<String, MutableMap<String, String>>().apply {
            days.forEach { day ->
                this[day] = mutableMapOf<String, String>().apply {
                    timeSlots.forEach { time -> this[time] = "" }
                }
            }
        })
    }
    // {Day: {Time: Course} }
    var nonEmpty by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    fun add(entry: String){
        if (!nonEmpty.contains(entry)){
            nonEmpty.add(entry)
        }
    }



    var scope = rememberCoroutineScope()
    var myCourses: List<Course> = listOf()
    routinevm.getMyCourses { list ->
        myCourses =  list
    }
    fun getCurrentSlot(){

    }

    LaunchedEffect(myCourses) {
        println(currentDay)
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

        println(nonEmpty.sorted())
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 130.dp)
        ) {
            items(days){ key ->
                if (nonEmpty.contains(key)){
                    Day(key, (map[key]!!))
                }

            }
        }
    }


}

@Composable
fun Day(day: String, map: MutableMap<String, String>){
    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
        ) {
            DayCard(day = day)
            RoutineRow()
            timeSlots.forEach { key ->
                if(map[key] != null && map[key] != ""){
                    RoutineRow(time = key, data = map[key]!!)
                }
            }
        }
    }
}

@Composable
fun RoutineRow(
    time: String = "",
    data: String = "",
    isNow: Boolean = false
){
    val temp = data.split(",")
    val course: String
    val section: String
    val room: String
    val t: String
    if(time!= ""){
        course = temp[0]
        section = temp[1]
        room = temp[2]
        t = time
    }
    else{
        course = "Course"
        section = "Sec"
        room = "Room"
        t  = "Class Time"
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(5.dp),
        colors =
        if (time == "") CardDefaults.cardColors(paletteDarkGreen)
        else if(isNow) CardDefaults.cardColors(paletteLightPurple)
        else CardDefaults.cardColors(paletteGreen),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        )
    ) {
        Row{
            Box(
                modifier = Modifier
                    .width(170.dp),
                contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
            ){
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                    text = t)
            }
            Box(
                modifier = Modifier
                    .width(80.dp),
                contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
            ){
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                    text = course)
            }
            Box(
                modifier = Modifier
                    .width(40.dp),
                contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
            ){
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                    text = section)
            }
            Box(
                modifier = Modifier
                    .width(90.dp),
                contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
            ){
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                    text = room)
            }
        }
    }
}

@Composable
fun DayCard(day: String){
    val d = when(day) {
        "Su" -> "Sunday"
        "Mo" -> "Monday"
        "Tu" -> "Tuesday"
        "We" -> "Wednesday"
        "Th" -> "Thursday"
        "Fr" -> "Friday"
        "Sa" -> "Saturday"
        else -> "Unknown" // Handle unexpected inputs
    }

    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .width(380.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        colors = CardDefaults.cardColors(paletteDarkGreen2)
    ){
        Row(
            modifier = Modifier
                .padding(5.dp),

        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = d,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}