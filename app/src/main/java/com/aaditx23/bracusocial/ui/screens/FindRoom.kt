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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.viewmodels.RoomVM
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

    var showLoading by rememberSaveable {
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
        mutableIntStateOf(timeList.indexOf(getTimeSlot(currentTime, timeList, roomvm)))
    }
    var emptyClasses by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    var emptyLabs by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }
    var emptyAll by rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }


    Row{


        CustomNavDrawer(
            list = dayList,
            selectedIndex = selectedIndexDay,
            cardHeight = 56.20
        ) {i ->
            selectedIndexDay = i
            val t = timeList[selectedIndexTime]
            if (t != "Closed"){
                CoroutineScope(Dispatchers.IO).launch {
                    showLoading = true
                    emptyClasses = roomvm.getEmptyClass(
                        time = t,
                        day = dayList[selectedIndexDay].slice(0..1)
                    )
                    delay(200)
                    showLoading = false
                }

            }
            else{
                println("jaynai")
                emptyClasses = mutableListOf()
            }
        }
        CustomNavDrawer(
            list = timeList,
            selectedIndex = selectedIndexTime,
            cardHeight = 50.0,
            containerWidth = 145
        ) {i ->
            selectedIndexTime = i
            val t = timeList[selectedIndexTime]
            if (t != "Closed"){
                CoroutineScope(Dispatchers.IO).launch {
                    showLoading = true
                    emptyClasses = roomvm.getEmptyClass(
                        time = t,
                        day = dayList[selectedIndexDay].slice(0..1)
                    )
                    delay(200)
                    showLoading = false
                }

            }
            else{
                println("jaynai")
                emptyClasses = mutableListOf()
            }
        }



        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
        ) {
            if(showLoading){
                item{
                    CircularProgressIndicator()
                }
            }
            else{
                items(emptyClasses.size){ index ->
                    Text(text = "${index + 1}. ${emptyClasses[index]}")
                }
            }

        }
//        LazyColumn(
//            modifier = Modifier
//                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
//        ) {
//            items(allLab.size){ index ->
//                Text(text = "${index + 1}. ${allLab[index]}")
//            }
//
//        }
    }

}

@Composable
fun CustomNavDrawer(
    list: List<String>,
    selectedIndex: Int,
    cardHeight: Double = 40.0,
    containerWidth: Int = 80,
    fontSize: Int = 14,
    onClick: (i: Int)-> Unit
){
    Box(
        modifier = Modifier
            .width(containerWidth.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp)
        ) {
            list.forEachIndexed { i, s ->
                Card(
                    onClick = {
                        onClick(i)
                    },
                    colors = if (selectedIndex == i){
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    } else {
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    },
                    shape = RectangleShape,

                    modifier = Modifier
                        .height(cardHeight.dp)
                        .border(1.dp, Color.Black)
                        .fillMaxWidth()


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


fun getTimeSlot(time:String, slotList: List<String>, vm: RoomVM): String{
    println(time)

    slotList.forEachIndexed { _, s ->

        if(s!= "Closed"){
            val temp = s.split("-")
            val start = temp[0].trim()
            val end = temp[1].trim()
            val withStart = vm.compareTime(time, start) // should be >=0
            val withEnd = vm.compareTime(time, end) // should be <=0

            if (withStart >=0 && withEnd <= 0){
                return s
            }
        }
    }
    return "Closed"
}

