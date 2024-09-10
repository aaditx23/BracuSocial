package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.classTimeList
import com.aaditx23.bracusocial.labTimeList
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getCurrentTime(): String{
    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = SimpleDateFormat("HH:mm", Locale.getDefault())

    return time12.format(date.parse(currentTime.format(formatter))!!).uppercase()
}
fun getToday(): String{
    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE")
//    println("Current day ${currentTime.format(formatter)}")
    return currentTime.format(formatter)
}
fun to24Hours(time: String): String {
    // Split the time into its components
    val (timePart, amPm) = time.split(" ")
    val (hour, minute) = timePart.split(":").map { it.toInt() }

    // Convert hour based on AM/PM
    val hour24 = when {
        amPm == "AM" && hour == 12 -> 0
        amPm == "AM" -> hour
        amPm == "PM" && hour == 12 -> 12
        amPm == "PM" -> hour + 12
        else -> hour // This case should not occur with valid input
    }

    // Format the result as a two-digit hour and minute, specifying the Locale
    return String.format(Locale.US, "%02d:%02d", hour24, minute)
}

fun to24HoursPlus10(time: String): String {
    // Split the time into its components
    val (timePart, amPm) = time.split(" ")
    var (hour, minute) = timePart.split(":").map { it.toInt() }
    minute = minute.toInt() + 10

    // Convert hour based on AM/PM
    val hour24 = when {
        amPm == "AM" && hour == 12 -> 0
        amPm == "AM" -> hour
        amPm == "PM" && hour == 12 -> 12
        amPm == "PM" -> hour + 12
        else -> hour // This case should not occur with valid input
    }

    // Format the result as a two-digit hour and minute, specifying the Locale
    return String.format(Locale.US, "%02d:%02d", hour24, minute)
}

fun add10ToMinutes(time: String): String {
    val (timePart, amPm) = time.split(" ")
    var (hour, minute) = timePart.split(":").map { it.toInt() }
    minute = minute.toInt() + 10
    return String.format(Locale.US, "%02d:%02d %s", hour, minute, amPm)
}



fun compareTime(time1: String, time2: String, isNext: Boolean = false): Int{
    val t1 = to24Hours(time1)
    val t2 =
        if (isNext)  to24HoursPlus10(time2)
        else  to24Hours(time2)
    return t1.compareTo(t2)

}


fun getTimeSlot(slotList: List<String>): String{
    val time = getCurrentTime()
    slotList.forEachIndexed { _, s ->

        if(s!= "Closed"){
            val temp = s.split("-")
            val start = temp[0].trim()
            val end = temp[1].trim()
            val withStart = compareTime(time, start) // should be >=0
            val withStartNext = compareTime(time, end, true) // should be <=0
            val withEnd = compareTime(time, end) // should be <=0
//            println("$withStart start $withEnd end")
            if (withStart >=0 && withEnd <= 0){
                return s
            }
            else if(withEnd >=0 && withStartNext <=0){
                return add10ToMinutes(end)
            }
        }
    }
    return "Closed"
}
fun getClassSlot(): String{
    return getTimeSlot(classTimeList)
}

fun getLabSlot(): String{
    return getTimeSlot(labTimeList)
}