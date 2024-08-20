package com.aaditx23.bracusocial.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.repositories.getClassSlot
import com.aaditx23.bracusocial.backend.local.repositories.getLabSlot
import com.aaditx23.bracusocial.ui.screens.Routine.timeSlots
import com.aaditx23.bracusocial.ui.theme.palette3paste
import com.aaditx23.bracusocial.ui.theme.palette6MagicMint
import com.aaditx23.bracusocial.ui.theme.paletteDarkGreen
import com.aaditx23.bracusocial.ui.theme.paletteDarkGreen2
import com.aaditx23.bracusocial.ui.theme.paletteGreen
import com.aaditx23.bracusocial.ui.theme.paletteLightPurple


@Composable
fun Day(day: String, map: MutableMap<String, String>, isToday: Boolean){
    val combinedSlots = listOf(
        getClassSlot(),
        getLabSlot()
    )
    fun getCurrentSlot(time: String): Boolean{
        if (combinedSlots.contains(time)){
            return true
        }
        else{
            combinedSlots.forEach { result ->
                return if (result.length == 8){
                    time.contains(result)
                } else{
                    false
                }
            }
        }
        return false

    }
    ElevatedCard(
        modifier = Modifier
            .padding(bottom = 15.dp, start = 5.dp, end = 5.dp)
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(
            if (isToday) palette6MagicMint
            else palette3paste
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            DayCard(day = day)
            RoutineRow()
            timeSlots.forEach { key ->
                if(map[key] != null && map[key] != ""){
                    val isNow = getCurrentSlot(key) && isToday
                    RowProcessor(time = key, data = map[key]!!, isNow)

                }
            }
        }
    }
}
@Composable
fun RowProcessor(
    time: String = "",
    data: String = "",
    isNow: Boolean = false
){
    if (data.contains("|")){
        val temp = data.split("|")
        temp.forEachIndexed {index, info ->
            if(info != ""){
                RoutineRow(time, info, isNow, index==1)
            }
        }
    }
    else{
        RoutineRow(time, data, isNow, true)
    }
}
@Composable
fun RoutineRow(
    time: String = "",
    data: String = "",
    isNow: Boolean = false,
    showTime: Boolean = false
){
    val temp = data.split(",")
    var name: String
    var course: String
    var section: String
    var room: String
    var t: String
    // Default values
    name = "Name"
    course = "Course"
    section = "Sec"
    room = "Room"
    t = "Class Time"

    if (time != ""){
        if (temp.size != 3){

            println("DATA $data")
            val friendData = data.split(".")
            name = friendData[0]
            course = friendData[1]
            section = friendData[2]
            room = friendData[5]
        }
        else{
            println("else $temp")
            course = temp[0]
            section = temp[1]
            room = temp[2]

        }
        t = time
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(temp.size != 3 && t != "Class Time" && showTime){
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
            ){
                Text(
                    fontWeight = FontWeight.Normal,
                    text = t,
//                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors =
            if (time == "") CardDefaults.cardColors(paletteDarkGreen)
            else if (isNow) CardDefaults.cardColors(paletteLightPurple)
            else CardDefaults.cardColors(paletteGreen),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
            )
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .width(170.dp),
                    contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
                ) {
                    if (temp.size != 3) {
                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                            text = name,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                            text = t,
                            color = Color.Black
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(80.dp),
                    contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                        text = course,
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .width(40.dp),
                    contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                        text = section,
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .width(90.dp),
                    contentAlignment = Alignment.Center
//                    .border(1.dp, Color.Black)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                        text = room,
                        color = Color.Black
                    )
                }
            }

        }
    }
}

@Composable
fun DayCard(day: String){
    val d = when(day) {
        "Su" -> "Sunday"
        "Mo" -> "Monday"
        "Tu" -> "Tuesday"
        "We" -> "Wednesday"
        "Th" -> "Thursday"
        "Fr" -> "Friday"
        "Sa" -> "Saturday"
        else -> "Unknown" // Handle unexpected inputs
    }

    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .width(380.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        colors = CardDefaults.cardColors(paletteDarkGreen2)
    ){
        Row(
            modifier = Modifier
                .padding(5.dp),

            ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = d,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}