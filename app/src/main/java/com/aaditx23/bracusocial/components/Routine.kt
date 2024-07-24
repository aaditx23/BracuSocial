package com.aaditx23.bracusocial.components

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.ui.theme.palette2DarkRed
import com.aaditx23.bracusocial.ui.theme.palette3paste

val celHeight = 45.dp

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Routine(courseList: MutableList<Course>, topPadding: Dp = 350.dp ){

    val tableData: MutableList<MutableList<MutableState<MutableList<Course>>>> = MutableList(8) { row ->
        MutableList(7) { column ->
            mutableStateOf(mutableListOf<Course>())
        }
    }


    courseList.forEachIndexed { _, course ->
        val classSlot = getSlot(course.classDay!!, course.classTime!!)
        val lab = course.labDay
        val labSlot: MutableMap<String, MutableList<Int>>
        if (lab != "-"){
            labSlot = getSlot(course.labDay!!, course.labTime!!)
            val labColumn = labSlot["column"]
            val labRow = labSlot["row"]
            labColumn!!.forEachIndexed { _, r ->
                labRow!!.forEachIndexed { _, c ->

                    tableData[c][r].value.add(course)

                }
            }
        }
        val classColumn = classSlot["column"]
        val classRow = classSlot["row"]
        classColumn!!.forEachIndexed { _, r ->
            classRow!!.forEachIndexed { _, c ->
                tableData[c][r].value.add(course)

            }
        }


    }

    Table(data = tableData, topPadding)


}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Table(data: MutableList<MutableList<MutableState<MutableList<Course>>>>,
          topPadding: Dp = 350.dp
){
    var status by rememberSaveable {
        mutableStateOf(mutableListOf(true, false, false))   //empty, clash, valid
    }
    val days = listOf(
        "Time",
        "Saturday",
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday"
    )

    val time = listOf(
        "08:00 - 09:20",
        "09:30 - 10:50",
        "11:00 - 12:20",
        "12:30 - 01:50",
        "02:00 - 03:20",
        "03:30 - 04:50",
        "05:00 - 06:20",
        "06:30 - 08:00",
    )
    // Top Row
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val availableHeight = screenHeight - 430
    val boxModifier: Modifier =
        if(availableHeight < celHeight.value * 8){
            Modifier
                .padding(top = topPadding)
                .height(availableHeight.dp)
                .horizontalScroll(rememberScrollState())
//                .verticalScroll(rememberScrollState())
        }
        else{
            Modifier
                .padding(top = topPadding)
                .horizontalScroll(rememberScrollState())
//                .verticalScroll(rememberScrollState())
        }


    Box(
        modifier = boxModifier

    ) {
        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Column {
                Row {
                    days.forEachIndexed { _, d ->
                        Cell(text = d)
                    }
                }
                data.forEachIndexed { row, courseRow ->

                    Row {
                        Cell(text = time[row])
                        courseRow.forEachIndexed { _, courseList ->

                            Cell(
                                courseList = courseList.value,
                                modifier = Modifier
                                    .size(height = celHeight, width = 120.dp)
                                    .border(1.dp, Color.DarkGray)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Cell(
    modifier: Modifier = Modifier
        .size(height = celHeight, width = 120.dp)
        .border(1.dp, Color.DarkGray),
    courseList: MutableList<Course>? = null,
    text: String? = null,

){
    val color =
        if (courseList != null && courseList.size >1) palette2DarkRed
        else if (courseList != null && courseList.size ==1 ) palette3paste
        else Color.Transparent
    Box(
        modifier = modifier
            .background(color),
        contentAlignment = Alignment.Center,
    ){
        if (courseList != null){
            if (courseList.isEmpty()){
                Text(text = "-")
            }
            else if (courseList.size >1){
                LazyColumn {
                    items(courseList){course ->
//                        val course = courseList[courseList.size-1 - index]
                        Text(
                            text = "${course.courseName} - ${course.section}",
                            color = Color.Black,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                        )
                    }
                }
            }

            else {
                courseList.forEachIndexed { _, course ->
                    Text(
                        text = "${course.courseName} - ${course.section}",
                        color = Color.Black
                    )
                }
            }
        }
        else{
            Text(text = text!!)
        }
    }

}

private fun getSlot(dayInfo: String, timeInfo: String): MutableMap<String, MutableList<Int>>{
    val day = dayInfo.split(" ")
    val column = mutableListOf<Int>()
    val row = mutableListOf<Int>()

    day.forEachIndexed { _, s ->
        when(s){
            "Sa" -> column.add(0)
            "Su" -> column.add(1)
            "Mo" -> column.add(2)
            "Tu" -> column.add(3)
            "We" -> column.add(4)
            "Th" -> column.add(5)
            "Fr" -> column.add(6)
        }
    }
    when(timeInfo){
        "08:00 AM - 09:20 AM" -> row.add(0)
        "09:30 AM - 10:50 AM" -> row.add(1)
        "11:00 AM - 12:20 PM" -> row.add(2)
        "12:30 PM - 01:50 PM" -> row.add(3)
        "02:00 PM - 03:20 PM" -> row.add(4)
        "03:30 PM - 04:50 PM" -> row.add(5)
        "05:00 PM - 06:20 PM" -> row.add(6)
        "06:30 PM - 08:00 PM" -> row.add(7)
        "08:00 AM - 10:50 AM" -> {row.add(0); row.add(1)}
        "11:00 AM - 01:50 PM" -> {row.add(2); row.add(3)}
        "02:00 PM - 04:50 PM" -> {row.add(4); row.add(5)}
        "05:00 PM - 08:00 PM" -> {row.add(6); row.add(7)}
    }

    return mutableMapOf(
        "row" to row,
        "column" to column
    )

}