package com.aaditx23.bracusocial.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.filterCourseByDays
import com.aaditx23.bracusocial.backend.filterCourseByFaculty
import com.aaditx23.bracusocial.backend.filterCourseByNameSection
import com.aaditx23.bracusocial.backend.filterCourseByRooms
import com.aaditx23.bracusocial.backend.filterCourseByTimes
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.checkInternetConnection
import com.aaditx23.bracusocial.components.CourseItem
import com.aaditx23.bracusocial.components.NoButtonDialog
import com.aaditx23.bracusocial.components.SearchBarDropDown
import com.aaditx23.bracusocial.dayList
import com.aaditx23.bracusocial.days
import com.aaditx23.bracusocial.timeSlots
import kotlinx.coroutines.launch

@Composable
fun CourseScreen(dbStatus: Boolean){
    val courseVM: CourseVM = hiltViewModel()
    val context = LocalContext.current

    val allCourses by courseVM.allCourses.collectAsState()
    var status by rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourseList by remember { mutableStateOf(allCourses) }
    var isFiltering by remember { mutableStateOf(false) }

    var totalCourses by remember { mutableIntStateOf(0) }
    var addedCourses by remember { mutableIntStateOf(0) }
    var hasInternet by remember{
        mutableStateOf(checkInternetConnection(context))
    }
    val filter by rememberSaveable {
        mutableStateOf(listOf(
            "Course-Section",
            "Faculty",
            "Class Day",
            "Class Time",
            "Class Room"
        ))
    }
    var currentFilter by rememberSaveable {
        mutableStateOf(filter[0])
    }
    var filterValue by rememberSaveable {
        mutableStateOf("")
    }


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
        if(!(currentFilter == filter[2] || currentFilter == filter[3])){
            coroutineScope.launch {
                isFiltering = true

                filteredCourseList = when (currentFilter) {
                    filter[0] -> filterCourseByNameSection(
                        list = allCourses,
                        searchQuery = searchQuery.text
                    )

                    filter[1] -> filterCourseByFaculty(
                        list = allCourses,
                        searchQuery = searchQuery.text
                    )

                    filter[4] -> filterCourseByRooms(
                        list = allCourses,
                        searchQuery = searchQuery.text
                    )

                    else -> filteredCourseList
                }
                isFiltering = false

            }
        }
    }
    LaunchedEffect(currentFilter, filterValue) {
        if(currentFilter == filter[2] || currentFilter == filter[3]){
            coroutineScope.launch {
                isFiltering = true
                filteredCourseList = when (currentFilter) {
                    filter[2] -> {
                        filterValue =
                            if (filterValue == "") dayList[0].slice(0..1)
                            else filterValue
                        filterCourseByDays(
                            list = allCourses,
                            searchQuery = filterValue.slice(0..1)
                        )
                    }

                    filter[3] -> {
                        filterValue =
                            if(filterValue == "") timeSlots[0]
                            else filterValue
                        filterCourseByTimes(list = allCourses, searchQuery = filterValue)
                    }
                    else -> filteredCourseList
                }
                println("After filtration $filter $filterValue ${filteredCourseList.size}")
                isFiltering = false

            }
        }
    }


    LaunchedEffect(dbStatus) {
        coroutineScope.launch {
            if (!dbStatus && hasInternet){
                refresh()
            }
        }
        println("HAS INTERNET $hasInternet")

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

        SearchBarDropDown(
            searchAction = { query ->
                searchQuery = query
            },
            dropDownAction = { filter ->
                currentFilter = filter
            },
            setFilterValue = { value ->
                filterValue = value
                println("FILTER VALUE $filterValue")

            },
            width = (LocalConfiguration.current.screenWidthDp -150).dp,
            height = 40.dp,
            textSize = 16.sp,
            dropDown = filter,
            text = "Search by...",
            currentFilterSelection = currentFilter
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
            NoButtonDialog(
                title = "Collecting courses",
                message = "Please wait\n$status",
                total = totalCourses,
                done = addedCourses
            )
        }
        else if(!hasInternet){
            println("NO INTERNET")
            NoButtonDialog(
                title = "No Internet",
                message = "Please connect to the internet and restart the application.",
                total = totalCourses,
                done = addedCourses
            )
        }
        else{
            LazyColumn() {
                items(
                    filteredCourseList
                ){course ->
                    CourseItem(course = course)
                }
            }
        }
    }
}

