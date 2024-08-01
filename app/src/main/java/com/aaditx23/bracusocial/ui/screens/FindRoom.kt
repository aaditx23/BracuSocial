package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.repositories.compareTime
import com.aaditx23.bracusocial.backend.viewmodels.RoomVM
import com.aaditx23.bracusocial.ui.theme.Purple80
import com.aaditx23.bracusocial.ui.theme.palette2DarkPurple2
import com.aaditx23.bracusocial.ui.theme.palette2DarkPurple3
import com.aaditx23.bracusocial.ui.theme.palette6LightIndigo
import com.aaditx23.bracusocial.ui.theme.palette7Blue1
import com.aaditx23.bracusocial.ui.theme.paletteLightPurple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FindRoom(){
    val roomvm: RoomVM = hiltViewModel()

    val allClass = roomvm.allClassRooms.collectAsState().value.toList()
    val allLab = roomvm.allLabRooms.collectAsState().value.toList()
    var scrollState = rememberScrollState()

    val dayList = listOf(
        "Saturday",
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday"
    )
    val timeList = listOf(
        "08:00 AM - 09:20 AM",
        "09:30 AM - 10:50 AM",
        "11:00 AM - 12:20 PM",
        "12:30 PM - 01:50 PM",
        "02:00 PM - 03:20 PM",
        "03:30 PM - 04:50 PM",
        "05:00 PM - 06:20 PM",
        "06:30 PM - 08:00 PM",
        "Closed"
    )
    val labTimeList = listOf(
        "08:00 AM - 10:50 AM",
        "11:00 AM - 01:50 PM",
        "02:00 PM - 04:50 PM",
        "05:00 PM - 08:00 PM",
        "Closed"
    )
    var clicked by rememberSaveable {
        mutableStateOf(false)
    }
    var showLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var isLab by rememberSaveable {
        mutableStateOf(false)
    }
    var today by rememberSaveable {
        mutableStateOf(roomvm.getToday())
    }
    var currentTime by rememberSaveable {
        mutableStateOf(roomvm.getCurrentTime())
    }
    var selectedIndexDay by rememberSaveable {
        mutableIntStateOf(dayList.indexOf(today))
    }
    var selectedIndexTime by rememberSaveable {
        mutableIntStateOf(timeList.indexOf(getTimeSlot(currentTime, timeList)))
    }
    var selectedIndexTimeLab by rememberSaveable {
        mutableIntStateOf(labTimeList.indexOf(getTimeSlot(currentTime, labTimeList)))
    }
    var emptyClasses by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    var emptyLab by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    val scope = rememberCoroutineScope()

    fun getClasses(){
        scope.launch {
            showLoading = true
            emptyClasses = roomvm.getEmptyClass(
                time = timeList[selectedIndexTime],
                day = dayList[selectedIndexDay].slice(0..1)
            )
            showLoading = false
        }
    }
    fun getLabs(){
        scope.launch {
            showLoading = true
            emptyLab = roomvm.getEmptyLab(
                time = labTimeList[selectedIndexTimeLab],
                day = dayList[selectedIndexDay].slice(0..1)
            )
//            delay(200)
            showLoading = false
        }
    }

    LaunchedEffect(Unit) {
        if(selectedIndexDay != 8) getClasses()
    }

    Row{
        //for day
        CustomNavDrawer(
            list = dayList,
            selectedIndex = selectedIndexDay,
            cardHeight = 66.0, //462,
//            unselectedColor = Purple80,
//            selectedColor = paletteLightPurple
        ) {i ->
            selectedIndexDay = i
            if (timeList[selectedIndexTime] != "Closed"){
                getClasses()
            }
            else{
                emptyClasses = mutableListOf()
            }
        }
        // for time slot
        if (isLab){
            CustomNavDrawer(
                list = labTimeList,
                selectedIndex = selectedIndexTimeLab,
                cardHeight = 46.0, //414
                containerWidth = 145,
//                selectedColor = palette2DarkPurple2,
//                unselectedColor = palette2DarkPurple3
            ) {i ->
                selectedIndexTimeLab = i
                val t = labTimeList[selectedIndexTimeLab]
                if (t != "Closed"){
                    getLabs()
                }
                else{
                    emptyLab = mutableListOf()
                }
            }


        }
        else{
            CustomNavDrawer(
                list = timeList,
                selectedIndex = selectedIndexTime,
                cardHeight = 46.0, //414
                containerWidth = 145,
//                selectedColor = palette2DarkPurple2,
//                unselectedColor = palette2DarkPurple3
            ) {i ->
                selectedIndexTime = i
                if (timeList[selectedIndexTime] != "Closed"){
                    getClasses()
                }
                else{
                    emptyClasses = mutableListOf()
                }
            }

        }




        LazyColumn(
            modifier = Modifier
                .padding(top = 130.dp, bottom = 150.dp, start = 30.dp)
        ) {
            if(showLoading){
                item{
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                }
            }
            else{
                if (isLab){
                    items(emptyLab.size) { index ->
                        Text(text = "${index + 1}. ${emptyLab[index]}")
                    }
                }
                else{
                    items(emptyClasses.size) { index ->
                        Text(text = "${index + 1}. ${emptyClasses[index]}")
                    }
                }
            }

        }
    }
    Button(
        modifier = Modifier
            .padding(top = 75.dp)
            .fillMaxWidth(),
        onClick = {
            isLab = !isLab
            clicked = false
            if (isLab){
                selectedIndexTimeLab = selectedIndexTime/2
                println(selectedIndexTimeLab)
                getLabs()
            }
            else{
                getClasses()
            }

        },
        shape = RectangleShape
    ) {
        Text(text = if (isLab) "Showing Lab" else "Showing Classes")
    }
}


@Composable
fun CustomNavDrawer(
    list: List<String>,
    selectedIndex: Int,
    cardHeight: Double = 40.0,
    containerWidth: Int = 80,
    fontSize: Int = 14,
    selectedColor: Color = MaterialTheme.colorScheme.inversePrimary,
    unselectedColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (i: Int)-> Unit
){
    var padding = 130
    if (list.size == 9 ){
        padding = 154
    }
    else if (list.size == 5){
        padding = 246
    }
    Box(
        modifier = Modifier
            .width(containerWidth.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = padding.dp)
        ) {
            list.forEachIndexed { i, s ->
                Card(
                    onClick = {
                        onClick(i)
                    },
                    colors = if (selectedIndex == i){
                        CardDefaults.cardColors(selectedColor)
                    } else {
                        CardDefaults.cardColors(unselectedColor)
                    },
                    shape = RectangleShape,

                    modifier = Modifier
                        .height(cardHeight.dp)
                        .border(1.dp, palette7Blue1)
                        .fillMaxWidth(),


                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = s,
                            fontSize = fontSize.sp
                        )
                    }
                }
            }
        }
    }
}



fun getTimeSlot(time:String, slotList: List<String>): String{
    println(time)

    slotList.forEachIndexed { _, s ->

        if(s!= "Closed"){
            val temp = s.split("-")
            val start = temp[0].trim()
            val end = temp[1].trim()
            val withStart = compareTime(time, start) // should be >=0
            val withEnd = compareTime(time, end) // should be <=0

            println("$time $start $end")
            if (withStart >=0 && withEnd <= 0){
                return s
            }
        }
    }
    return "Closed"
}

