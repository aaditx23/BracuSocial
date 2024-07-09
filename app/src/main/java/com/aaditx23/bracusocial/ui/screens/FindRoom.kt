package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.viewmodels.RoomVM
import com.aaditx23.bracusocial.components.NavDrawer
import com.aaditx23.bracusocial.components.models.NavDrawerItem

@Composable
fun FindRoom(){
    val roomvm: RoomVM = hiltViewModel()

    val allClass = roomvm.allClassRooms.collectAsState().value.toList()
    val allLab = roomvm.allLabRooms.collectAsState().value.toList()
    var scrollState = rememberScrollState()
    var selectedIndexDay by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexTime by rememberSaveable {
        mutableIntStateOf(0)
    }

    val dayList = listOf(
        "Today",
        "Saturday",
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday"
    )
    val timeList = listOf(
        "Current Time",
        "08:00 - 09:20",
        "09:30 - 10:50",
        "11:00 - 12:20",
        "12:30 - 01:50",
        "02:00 - 03:20",
        "03:30 - 04:50",
        "05:00 - 06:20",
        "06:30 - 08:00",
    )




    Row{

        CustomNavDrawer(list = dayList, selectedIndex = selectedIndexDay) {i ->
            selectedIndexDay = i
        }
        CustomNavDrawer(list = timeList, selectedIndex = selectedIndexTime) {i ->
            selectedIndexTime = i
        }



        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
        ) {
            items(allClass.size){ index ->
                Text(text = "${index + 1}. ${allClass[index]}")
            }

        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
        ) {
            items(allLab.size){ index ->
                Text(text = "${index + 1}. ${allLab[index]}")
            }

        }
    }

}

@Composable
fun CustomNavDrawer(
    list: List<String>,
    selectedIndex: Int,
    onClick: (i: Int)-> Unit
){
    Box(
        modifier = Modifier
            .width(120.dp)
            .padding(start = 5.dp)
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
                        .height(40.dp)
                        .border(1.dp, Color.Black)
                        .fillMaxWidth()


                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = s)
                    }
                }
            }
        }
    }
}
