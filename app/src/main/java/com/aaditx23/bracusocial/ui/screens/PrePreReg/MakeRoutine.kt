package com.aaditx23.bracusocial.ui.screens.PrePreReg

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.components.FilterCourseList
import com.aaditx23.bracusocial.components.Routine
import com.aaditx23.bracusocial.components.SearchBar
import com.aaditx23.bracusocial.ui.theme.paletteDarkGreen2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MakeRoutine(
    coursevm : CourseVM,
    courseList: List<Course>,
    selectedCourseList: MutableList<Course>,
    selectedMap: MutableMap<String, Boolean>,
    addCourse: (course: Course) -> Unit,
    removeCourse: (course: Course) -> Unit,
    clearRoutine: () -> Unit,
    fromProfile: Boolean = false

){
    val accountvm: AccountVM = hiltViewModel()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourseList by remember { mutableStateOf(courseList) }
    var isFiltering by remember { mutableStateOf(false) }
    var hasClash by rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()


    fun saveOrSetCourse(save: Boolean){
        if (selectedCourseList.isEmpty()) {
            Toast.makeText(
                context,
                "Empty routine cannot be saved",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var savedRoutine = ""
            var sortedList = mutableListOf<String>()
            selectedCourseList.forEachIndexed { _, course ->
                sortedList.add("${course.courseName} ${course.section}")
            }
            sortedList.sort()
            sortedList.forEachIndexed { i, string ->
                savedRoutine = if (i == 0) {
                    string
                } else {
                    "$savedRoutine,$string"
                }
            }
            if (save){
                coursevm.addSavedRoutine(savedRoutine)
                Toast.makeText(context, "Routine Saved", Toast.LENGTH_SHORT).show()
            }
            else{
                accountvm.addCourses(savedRoutine)
                Toast.makeText(context, "Routine Added to Profile", Toast.LENGTH_SHORT).show()

            }
            clearRoutine()

        }
    }
    // Filtering logic within a coroutine
    LaunchedEffect(searchQuery) {
        coroutineScope.launch {
            isFiltering = true
            filteredCourseList = FilterCourseList(list = courseList, searchQuery = searchQuery.text)
            isFiltering = false

        }
    }
    //All Courses
    Column(modifier = Modifier.padding(top = 70.dp)){

        Row(
            modifier = Modifier
                .padding(top = 10.dp)
        ) {
            Column{
                SearchBar(
                    action = { query ->
                        searchQuery = query
                    },
                    width = 150.dp,
                    paddingStart = 5.dp,
                    paddingEnd = 0.dp,
                    textSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .size(height = 230.dp, width = 150.dp)
                        .padding(start = 5.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                ) {

                    if (isFiltering) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn {
                            items(
                                if (searchQuery.text == "") {
                                    courseList
                                } else {
                                    filteredCourseList
                                }
                            ) { course ->
                                CourseCard(
                                    course = course,
                                    selectedMap = selectedMap,
                                    courseAction = addCourse,
                                    left = true
                                )
                            }
                        }
                    }
                }
            }

            //Selected Courses
            Column{
                //40dp
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp)
                        .padding(start = 5.dp, end = 5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = if (hasClash) "Clash" else "",
                        color = Color.Red
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(height = 230.dp, width = 150.dp)

                        .border(
                            1.dp,
                            if (hasClash) Color.Red
                            else if (selectedCourseList.size == 0) Color.Gray
                            else paletteDarkGreen2,
                            RoundedCornerShape(5.dp)
                        )
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
            }

            Column(
                modifier = Modifier
                    .padding(top = 50.dp, start = 5.dp)
            ) {
                if(fromProfile){
                    Button(onClick = {
                        if(hasClash){
                            Toast.makeText(
                                context,
                                "Resolve clash to add to profile",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            saveOrSetCourse(save = false)
                        }

                        }
                    ) {
                        Text(text = "Add")
                    }
                }
                else{
                    Button(onClick = { saveOrSetCourse(save = true) }) {
                        Text(text = "Save")
                    }
                    Button(onClick = { clearRoutine() }) {
                        Text(text = "Clear")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.padding(top = 10.dp)
        ){
        Routine(
            courseList = selectedCourseList,
            getClash = {clash ->
                hasClash = clash
            }
        )
    }


}

@Composable
fun CourseCard(
    course: Course,
    selectedMap: MutableMap<String, Boolean> = mutableMapOf(),
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