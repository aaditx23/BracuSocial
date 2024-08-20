package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.timeSlots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class RoomVM @Inject constructor(
    private val courseR: CourseRepository,
): ViewModel() {

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
        println("Current day ${currentTime.format(formatter)}")
        return currentTime.format(formatter)
    }

    suspend fun getEmptyClass(time: String, day: String): MutableList<String> {
        val courseList = withContext(Dispatchers.IO) {
            courseR.findOccupiedClass(time, day)
        }
        val newList = mutableListOf<String>()
        val allClassRooms = courseR.getAllClass().first()
        allClassRooms.forEachIndexed { _, s ->
            if (!courseList.contains(s)) {
                newList.add(s.toString())
            }
        }
        return newList
    }
    suspend fun getEmptyLab(time: String, day: String): MutableList<String> {
        val occupiedLabs = withContext(Dispatchers.IO) {
            courseR.findOccupiedLab(time, day)
        }
        val emptyLabs = mutableListOf<String>()
        val allLabRooms = courseR.getAllLab().first()
        allLabRooms.forEachIndexed { _, s ->
            if (!occupiedLabs.contains(s)) {
                emptyLabs.add(s.toString())
            }
        }
        return emptyLabs
    }

    fun compareTime(t1: String, t2: String): Int{
        println("$t1 $t2 time before format")
//        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
//        val localTime1 = LocalTime.parse(t1, formatter)
//        val localTime2 = LocalTime.parse(t2, formatter)

        return t1.compareTo(t2)
    }



}