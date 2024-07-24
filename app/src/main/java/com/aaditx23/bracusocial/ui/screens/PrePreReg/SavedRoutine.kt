package com.aaditx23.bracusocial.ui.screens.PrePreReg

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.SavedRoutineVM
import com.aaditx23.bracusocial.components.Routine
import com.aaditx23.bracusocial.ui.theme.palette3paste
import com.aaditx23.bracusocial.ui.theme.palette4green
import com.aaditx23.bracusocial.ui.theme.paletteGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SavedRoutine(
    coursevm : CourseVM
){
    val savedRoutinevm: SavedRoutineVM = hiltViewModel()
    val allRoutines = savedRoutinevm.allSavedRoutines.collectAsState().value.toList()
//    val coursevm : CourseVM = hiltViewModel()
    val allCourses by coursevm.allCourses.collectAsState()
    
    val courseMap by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Course>())
    }
    var routineIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var courseList by rememberSaveable {
        mutableStateOf(mutableListOf<Course>())
    }
    val context = LocalContext.current

    LaunchedEffect(allCourses) {
        CoroutineScope(Dispatchers.IO).launch {
//            isLoading = true
            delay(500)
            if (!allCourses.isEmpty()){
                allCourses.forEachIndexed { index, course ->
                    val key = "${course.courseName} ${course.section}"
                    courseMap[key] = course
                }
                isLoading = false
            }

        }

    }


    

    
    if (isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            CircularProgressIndicator()
        }
    }
    else if (allRoutines.isEmpty()){

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){ Text(text = "No saved routines") }
    }
    else{
        courseList = lineToList(
            allCourses = courseMap,
            line = allRoutines[routineIndex].courseKeyList
        )
        Column(
        ) {
            Box(
                modifier = Modifier
                    .height(230.dp)
                    .padding(top = 80.dp, start = 5.dp, end = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .padding(start = 10.dp, end = 10.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(palette4green)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 10.dp),
                            contentAlignment = Alignment.Center,
                        ){
                            Text(
                                text = "Added\nCourses",
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }

                    }
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxHeight(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(palette3paste)
                    ){
                        LazyColumn {
                            items(courseList) { course ->
                                CourseItem(course = course)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                Button(
                    onClick = {
                        if (routineIndex >0 ){
                            routineIndex--
                        }
                    },
                    enabled = routineIndex != 0
                ) {
                    Text(text = "<")
                }
                Text(
                    text = "${routineIndex+1}/${allRoutines.size}",
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Button(
                    onClick = {
                        if (routineIndex < allRoutines.size -1){
                            routineIndex++
                        }
                    },
                    enabled = routineIndex != allRoutines.size -1
                ) {
                    Text(text = ">")
                }
            }
            Button(
                onClick = {
                    savedRoutinevm.removeSavedRoutine(allRoutines[routineIndex]._id)
                    Toast.makeText(context, "Routine deleted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(text = "Delete Routine")
                Icon(imageVector = Icons.Filled.Cancel, contentDescription = "Unfriend")
            }

            Routine(
                courseList = courseList,
                topPadding = 10.dp
            )


        }
    }
    
}

private fun lineToList(allCourses: MutableMap<String, Course>, line: String): MutableList<Course>{
    val temp = line.split(",")
    val list = mutableListOf<Course>()
    temp.forEachIndexed { _, s ->
        list.add(allCourses[s]!!)
    }
    return list
}

@Composable
fun CourseItem(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(paletteGreen)
    ) {
        Text(
            text = "${course.courseName}, Section: ${course.section}",
            modifier = Modifier
                .padding(5.dp),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}