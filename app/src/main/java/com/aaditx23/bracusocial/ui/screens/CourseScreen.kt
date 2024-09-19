package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.aaditx23.bracusocial.backend.filterCourses
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.checkInternetConnection
import com.aaditx23.bracusocial.components.CallCourseItem
import com.aaditx23.bracusocial.components.DropDownCard

import com.aaditx23.bracusocial.components.NoButtonDialog
import com.aaditx23.bracusocial.components.SearchBar

import com.aaditx23.bracusocial.dayList
import com.aaditx23.bracusocial.listfiltersAddAll
import com.aaditx23.bracusocial.timeSlots
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue4
import com.aaditx23.bracusocial.ui.theme.paletteBlue6
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CourseScreen(dbStatus: Boolean){
    val courseVM: CourseVM = hiltViewModel()
    val context = LocalContext.current
    val dayFilters = listfiltersAddAll(dayList, "All Days")
    val timeFilters = listfiltersAddAll(timeSlots, "All Time Slots")

    val allCourses by courseVM.allCourses.collectAsState()
    var status by rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var filteredCourseList by remember { mutableStateOf(allCourses) }
    var isFiltering by remember { mutableStateOf(false) }

    var totalCourses by remember { mutableIntStateOf(0) }
    var addedCourses by remember { mutableIntStateOf(0) }
    var hasInternet by remember{
        mutableStateOf(checkInternetConnection(context))
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var searchQueryFaculty by remember { mutableStateOf(TextFieldValue("")) }
    var searchQueryRoom by remember { mutableStateOf(TextFieldValue("")) }
    var filterDay by rememberSaveable {
        mutableStateOf(dayFilters[0])
    }
    var filterTime by rememberSaveable {
        mutableStateOf(timeFilters[0])
    }


    val coroutineScope = rememberCoroutineScope()

    fun refresh(){
        println("refreshing")
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
    LaunchedEffect(searchQuery, searchQueryFaculty, searchQueryRoom, filterDay, filterTime) {
        isFiltering = true

        filteredCourseList = withContext(Dispatchers.IO) {
            // If all search fields are blank, return all courses

            // Filter the courses based on the provided criteria
            filterCourses(
                list = allCourses,
                searchQuery = searchQuery.text,
                days = filterDay,
                time = filterTime,
                room = searchQueryRoom.text,
                faculty = searchQueryFaculty.text
            )

        }

        isFiltering = false
    }



    LaunchedEffect(dbStatus) {
        coroutineScope.launch {
            if (!dbStatus && hasInternet){
                refresh()
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 80.dp, bottom = 20.dp)
    ) {
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

        // Filters drop down

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            DropDownCard(
                dropdownItems = dayFilters,
                startPadding = 8.dp,
                endPadding = 8.dp,
                onItemClick = { day ->
                    filterDay = day
                },
                bgColor = paletteBlue4,
                fontColor = paletteBlue1,
                weight = 0.48f
            )
            DropDownCard(
                dropdownItems = timeFilters,
                startPadding = 8.dp,
                endPadding = 8.dp,
                onItemClick = { time ->
                    filterTime = time
                },
                bgColor = paletteBlue4,
                fontColor = paletteBlue1,
                weight = 1f
            )
        }
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            SearchBar(
                action = { query ->
                    searchQueryRoom = query
                },
                height = 40.dp,
                paddingStart = 8.dp,
                paddingEnd = 8.dp,
                textSize = 15.sp,
                text = "Search By room...",
                weight = 0.5f
            )
            SearchBar(
                action = { query ->
                    searchQueryFaculty = query
                },
                paddingStart = 8.dp,
                paddingEnd = 8.dp,
                height = 40.dp,
                textSize = 14.sp,
                text = "Search By Faculty...",
                weight = 1f
            )
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
            NoButtonDialog(
                title = "Collecting courses",
                message = "Please wait\n$status",
                total = totalCourses,
                done = addedCourses
            )
        }
        else if(!hasInternet && !dbStatus){
            println("NO INTERNET")
            NoButtonDialog(
                title = "No Internet",
                message = "Please connect to the internet and restart the application.",
                total = totalCourses,
                done = addedCourses
            )
        }
        else{
            LazyColumn(
                modifier = Modifier
                    .height((LocalConfiguration.current.screenHeightDp-20).dp)

            ) {
                items(
                    if(
                        searchQuery.text.isBlank() &&
                        searchQueryFaculty.text.isBlank() &&
                        searchQueryRoom.text.isBlank() &&
                        filterDay.contains("All") &&
                        filterTime.contains("All")
                    )
                        allCourses
                    else
                    filteredCourseList
                ){course ->
                    CallCourseItem(courseData = course)
                }
            }
        }
    }
}

