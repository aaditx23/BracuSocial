package com.aaditx23.bracusocial.backend.local.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.mongodb.kbson.ObjectId
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

    val allCourses = courseR.getAllCourses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val allClassRooms = courseR.getAllClass()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptySet()
        )
    val allLabRooms = courseR.getAllLab()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptySet()
        )


    fun getCurrentTime(): String{
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = SimpleDateFormat("HH:mm", Locale.getDefault())
        return time12.format(date.parse(currentTime.format(formatter))!!)
    }
    fun getToday(): String{
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE")
        return currentTime.format(formatter)
    }

    suspend fun getEmptyClass(time: String, day: String): MutableList<String> {
        val courseList = withContext(Dispatchers.IO) {
            courseR.findOccupiedClass(time, day)
        }
        val newList = mutableListOf<String>()
        allClassRooms.value.forEachIndexed { _, s ->
            if (!courseList.contains(s)) {
                newList.add(s.toString())
            }
        }
        return newList
    }
    suspend fun getEmptyLab(time: String, day: String): MutableList<String> {
        val courseList = withContext(Dispatchers.IO) {
            courseR.findOccupiedLab(time, day)
        }
        val newList = mutableListOf<String>()
        allLabRooms.value.forEachIndexed { _, s ->
            if (!courseList.contains(s)) {
                newList.add(s.toString())
            }
        }
        return newList
    }

    fun compareTime(t1: String, t2: String): Int{
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val localTime1 = LocalTime.parse(t1, formatter)
        val localTime2 = LocalTime.parse(t2, formatter)

        return localTime1.compareTo(localTime2)
    }


}