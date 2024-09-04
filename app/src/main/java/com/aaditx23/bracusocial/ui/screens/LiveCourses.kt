package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.checkInternetConnection
import com.aaditx23.bracusocial.components.CourseItemJson
import com.aaditx23.bracusocial.components.EmptyScreenText
import com.aaditx23.bracusocial.components.FilterCourseList
import com.aaditx23.bracusocial.components.FilterCourseListJson
import com.aaditx23.bracusocial.components.NoButtonCircularLoadingDialog
import com.aaditx23.bracusocial.components.SearchBar
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

    var courseJsonList by remember{
        mutableStateOf(mutableListOf<JSONObject>())
    }
    var hasInternet by remember{
        mutableStateOf(false)
    }

    var toggleRefresh by remember {
        mutableStateOf(false)
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourseList by remember { mutableStateOf(courseJsonList) }
    var isFiltering by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(searchQuery) {
        scope.launch {
            isFiltering = true
            filteredCourseList = FilterCourseListJson(list = courseJsonList, searchQuery = searchQuery.text).toMutableList()
            isFiltering = false

        }
    }

    LaunchedEffect(toggleRefresh) {
        scope.launch {
            if(hasInternet){
                courseJsonList = coursevm.liveData { loading ->
                    println("Loading $loading")
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
            .padding(top = 80.dp)
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
        SearchBar(
            action = { query ->
                searchQuery = query
            },
            width = LocalConfiguration.current.screenWidthDp.dp,
            height = 40.dp,
            textSize = 16.sp
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
                        courseJsonList
                    } else {
                        filteredCourseList
                    }
                ) { course ->
                    CourseItemJson(course)
                }
            }
        }
    }
}

