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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue2
import com.aaditx23.bracusocial.ui.theme.paletteBlue5
import com.aaditx23.bracusocial.ui.theme.paletteBlue7
import com.aaditx23.bracusocial.ui.theme.paletteBlue8
import com.aaditx23.bracusocial.ui.theme.paletteBlue9

fun checkEmptyDayForFriend(
    name:String,
    map: MutableMap<String, String>
): Boolean{
    var result = true
    if(name == "All Friends") return false

    println("map $map")

    for ((key, value) in map){
       val temp = value.split("|")
        println("Temp $temp")
       temp.forEachIndexed { _, s ->
           if (s.contains(name)){
               println(s)
               result = false
           }
       }
    }


    return result
}

@Composable
fun Day(day: String,
        map: MutableMap<String, String>,
        isToday: Boolean,
        myRoutine: Boolean = true,
        selectedFriend: String = "All Friends"
){
    val combinedSlots = listOf(
        getClassSlot(),
        getLabSlot()
    )
    var isEmpty by remember {
        mutableStateOf(checkEmptyDayForFriend(selectedFriend, map))
    }
    LaunchedEffect(selectedFriend) {
        isEmpty = checkEmptyDayForFriend(selectedFriend, map)
    }
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
            if (isToday) paletteBlue7
            else paletteBlue9
        )

    ) {
        @Composable
        fun callUI(){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                DayCard(day = day, isToday = isToday)
                RoutineRow(myRoutine = myRoutine)
                timeSlots.forEach { key ->
                    if(map[key] != null && map[key] != ""){
                        val isNow = getCurrentSlot(key) && isToday
                        RowProcessor(time = key, data = map[key]!!, isNow, myRoutine, selectedFriend)

                    }
                }
            }
        }

        if (!myRoutine){
            if(!isEmpty){
                println("day not empty $day")
                callUI()
            }
            else{
                println("day empty $day")
            }
        }
        else{
            callUI()
        }
    }
}
@Composable
fun RowProcessor(
    time: String = "",
    data: String = "",
    isNow: Boolean = false,
    myRoutine: Boolean,
    selectedFriend: String = "All Friends"
){
    if (data.contains("|")){
        val temp = data.split("|")
        temp.forEachIndexed {index, info ->
            @Composable
            fun callRoutineRow(myRoutine: Boolean){
                RoutineRow(
                    time = time,
                    data = info,
                    isNow = isNow,
                    showTime = index == 1,
                    myRoutine = myRoutine
                )
            }
            if(info != ""){
                if(!myRoutine){
                    if(selectedFriend == "All Friends"){
                        callRoutineRow(false)
                    }
                    else{
                        if (info.split(".")[0].trim() == selectedFriend){
                            callRoutineRow(false)
                        }
                    }

                }
                else{
                    callRoutineRow(true)
                }
            }
        }
    }
    else{
        RoutineRow(time = time,
            data = data,
            isNow = isNow,
            showTime = true,
            myRoutine = myRoutine)
    }
}
@Composable
fun RoutineRow(
    time: String = "",
    data: String = "",
    isNow: Boolean = false,
    showTime: Boolean = false,
    myRoutine: Boolean = true
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
    val infoFontColor =  if (time == "") paletteBlue8 else paletteBlue1
    val rowColor = if (time == "") CardDefaults.cardColors(paletteBlue2)
    else if (isNow) CardDefaults.cardColors(paletteBlue9)
    else CardDefaults.cardColors(paletteBlue5)




    if (time != ""){
        if (temp.size != 3){

//            println("DATA $data")
            val friendData = data.split(".")
            name = friendData[0]
            course = friendData[1]
            section = friendData[2]
            room = friendData[5]
        }
        else{
//            println("else $temp")
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
        if(temp.size != 3  && showTime){
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
            colors = rowColor,
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
//                        println("CLASS SCHEDULE ${temp.size} $temp $t")
                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                            text = if (myRoutine) t else name,
                            color = infoFontColor
                        )
                    } else {

                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            fontWeight = if (time == "") FontWeight.Bold else FontWeight.Normal,
                            text = t,
                            color = infoFontColor
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
                        color = infoFontColor
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
                        color = infoFontColor
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
                        color = infoFontColor
                    )
                }
            }

        }
    }
}

@Composable
fun DayCard(day: String, isToday: Boolean){
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
    val infoFontColor =  paletteBlue9

    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .width(380.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        colors = CardDefaults.cardColors(paletteBlue1)
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
                    color = infoFontColor
                )
            }
        }
    }
}