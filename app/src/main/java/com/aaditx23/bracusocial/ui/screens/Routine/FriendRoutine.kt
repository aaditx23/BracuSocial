package com.aaditx23.bracusocial.ui.screens.Routine

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.local.repositories.getToday
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import com.aaditx23.bracusocial.components.Day
import com.aaditx23.bracusocial.components.DropDownCard
import com.aaditx23.bracusocial.components.NoButtonCircularLoadingDialog
import com.aaditx23.bracusocial.components.SearchBarDropDown
import com.aaditx23.bracusocial.days
import com.aaditx23.bracusocial.ui.screens.DayBar
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FriendRoutine(routinevm: RoutineVM){

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var isEmpty by rememberSaveable {
        mutableStateOf(true)
    }
    var currentDay by rememberSaveable {
        mutableStateOf(getToday().slice(0..1))
    }
    var map by rememberSaveable {
        mutableStateOf(mutableMapOf<String, MutableMap<String, String>>())
    }
    // {Day: {Time: Name-Course-Sec-Room} }
    val nonEmpty by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    var friendList by remember{
        mutableStateOf(mutableSetOf<String>("All Friends"))
    }
    var selectedIndexDay by rememberSaveable {
        mutableIntStateOf(days.indexOf(currentDay))
    }
    var selectedFriend by remember{
        mutableStateOf("All Friends")
    }
    println("$currentDay $currentDay $selectedIndexDay $days")
    fun add(entry: String){
        if (!nonEmpty.contains(entry)){
            nonEmpty.add(entry)
        }
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    var listState = rememberLazyListState()

    LaunchedEffect(map) {
        scope.launch {
            routinevm.friendList { list -> friendList.addAll( list.map { it.studentName }) }

            println(friendList)
            routinevm.friendsAndCourses(
                setLoading = { b->
                    isLoading = b
                },
                setData = { data ->
                    map = data
                },
                setEmpty = { b->
                    isEmpty = b
                },
                addNonEmpty = { s->
                    add(s)
                }
            )
        }
    }
    LaunchedEffect(selectedIndexDay) {
        scope.launch {
            listState.animateScrollToItem(selectedIndexDay)
        }
    }
    if (isLoading){
        NoButtonCircularLoadingDialog(title = "Friends' Schedule", message = "Please wait while your friends' courses are being arranged...")
    }
    else{
        if (isEmpty){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "No friends added")
            }
        }
        else{
            Column(
                modifier = Modifier
                    .padding(top = 70.dp)
            ){

                DayBar(
                    list = days.subList(0, days.size-1),
                    selectedIndex = selectedIndexDay,
                    onClick = {i ->
                        selectedIndexDay = i
                        currentDay = days[selectedIndexDay]
                    },
                    topPadding = 10.dp
                )
                DropDownCard(
                    dropdownItems = friendList.toList(),
                    width = LocalConfiguration.current.screenWidthDp.dp,
                    startPadding = 15.dp,
                    endPadding = 15.dp,
                    onItemClick = { friend ->
                        selectedFriend = friend
                    }
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 10.dp,bottom = 130.dp),
                    state = listState
                ) {
                    items(days) { key ->
                        if (nonEmpty.contains(key)) {
                            Day(
                                day = key,
                                map = (map[key]!!),
                                isToday = key == currentDay,
                                myRoutine = false,
                                selectedFriend = selectedFriend
                            )
                            println("map key")
                        }

                    }
                }
            }
        }
    }
}