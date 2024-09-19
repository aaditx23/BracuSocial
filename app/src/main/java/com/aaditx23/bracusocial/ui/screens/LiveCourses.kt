package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.aaditx23.bracusocial.backend.filterJsonByDays
import com.aaditx23.bracusocial.backend.filterJsonByFaculty
import com.aaditx23.bracusocial.backend.filterJsonByNameSection
import com.aaditx23.bracusocial.backend.filterJsonByRooms
import com.aaditx23.bracusocial.backend.filterJsonByTimes
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.checkInternetConnection
import com.aaditx23.bracusocial.components.CallCourseItem
import com.aaditx23.bracusocial.components.EmptyScreenText
import com.aaditx23.bracusocial.components.NoButtonCircularLoadingDialog
import com.aaditx23.bracusocial.components.SearchBar
import com.aaditx23.bracusocial.components.SearchBarDropDown
import com.aaditx23.bracusocial.dayList
import com.aaditx23.bracusocial.timeSlots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

@SuppressLint("MutableCollectionMutableState")
@Composable
fun LiveFeed(){
    val coursevm: CourseVM = hiltViewModel()
    val context = LocalContext.current
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }

    var allCourses by remember{
        mutableStateOf(listOf<JSONObject>())
    }
    var hasInternet by remember{
        mutableStateOf(checkInternetConnection(context))
    }

    var toggleRefresh by remember {
        mutableStateOf(false)
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourseList by remember { mutableStateOf(allCourses) }
    var isFiltering by remember { mutableStateOf(false) }
    val filter by rememberSaveable {
        mutableStateOf(listOf(
            "Course-Section",
            "Faculty",
            "Day",
            "Time",
            "Room"
        ))
    }
    var currentFilter by rememberSaveable {
        mutableStateOf(filter[0])
    }
    var filterValue by rememberSaveable {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchQuery, currentFilter) {
        if(!(currentFilter == filter[2] || currentFilter == filter[3])){
            scope.launch {
                isFiltering = true

                filteredCourseList = async{
                    when (currentFilter) {
                        filter[0] -> filterJsonByNameSection(
                            list = allCourses,
                            searchQuery = searchQuery.text
                        ).toMutableList()

                        filter[1] -> filterJsonByFaculty(
                            list = allCourses,
                            searchQuery = searchQuery.text
                        ).toMutableList()

                        filter[4] -> filterJsonByRooms(
                            list = allCourses,
                            searchQuery = searchQuery.text
                        ).toMutableList()

                        else -> allCourses
                    }
                }.await()

                isFiltering = false
            }
        }

    }
    LaunchedEffect(currentFilter, filterValue) {
        if(currentFilter == filter[2] || currentFilter == filter[3]){
            scope.launch {
                isFiltering = true
                filteredCourseList = async{
                    when (currentFilter) {
                        filter[2] -> {
                            filterValue =
                                if (filterValue == "") dayList[0].slice(0..1)
                                else filterValue
                            filterJsonByDays(
                                list = allCourses,
                                searchQuery = filterValue.slice(0..1)
                            )
                        }

                        filter[3] -> {
                            filterValue =
                                if (filterValue == "") timeSlots[0]
                                else filterValue
                            filterJsonByTimes(list = allCourses, searchQuery = filterValue)
                        }

                        else -> allCourses
                    }
                }.await()
//                println("After filtration $filter $filterValue ${filteredCourseList.size}")
                isFiltering = false

            }
        }
    }

    LaunchedEffect(toggleRefresh) {
        scope.launch {
            if(hasInternet){
                allCourses = coursevm.liveData { loading ->
//                    println("Loading $loading")
                    isLoading = loading
                }
            }
            else{
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 80.dp, bottom = 30.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    toggleRefresh = !toggleRefresh
                    hasInternet = checkInternetConnection(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = "Refresh")
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

            },
            width = (LocalConfiguration.current.screenWidthDp -150).dp,
            height = 48.dp,
            textSize = 16.sp,
            primaryDropDown = filter,
            text = "Search by...",
            currentFilterSelection = currentFilter,
            secondaryDropDown = when (currentFilter) {
                "Day" -> dayList
                "Time" -> timeSlots
                else -> listOf("")
            }
        )
        if (isLoading) {
            NoButtonCircularLoadingDialog(
                title = "Connecting to BRACU",
                message = "Please wait while loading data from website"
            )
        } else if (!hasInternet) {
            println("Entered !hasInternet")
            EmptyScreenText("Please connect to the internet and refresh...")
        } else {
            LazyColumn {
                items(
                    if (searchQuery.text == "") {
                        allCourses
                    } else {
                        filteredCourseList
                    }
                ) { course ->
                    CallCourseItem(course)
                }
            }
        }
    }
}

