package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.timeSlots
import com.aaditx23.bracusocial.ui.screens.Routine.days
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class RoutineVM @Inject constructor(
    private val courseR: CourseRepository,
    private val fpr: FriendProfileRepository,
    private val profileR: ProfileRepository
): ViewModel() {

    fun getCourse(courseId: String, setCourse: (Course) -> Unit){
        val temp = courseId.split(" ")
        val name = temp[0].trim()
        val sec = temp[1].trim()
        viewModelScope.launch {
            val course = courseR.findCourse(name, sec)
            if (course != null) {
                setCourse(course)
            }
        }
    }
    fun getMyCourses(setMyCourseList: (List<Course>) -> Unit){
        viewModelScope.launch {
            val me = profileR.getMyProfile()
            if (me != null){
                val temp = me.enrolledCourses.split(",")
                if (temp.isEmpty() || temp.all { it == "" }){
                    setMyCourseList(listOf())
                }
                else{
                    val courseList = mutableListOf<Course>()
                    temp.forEach { s ->
//                    println("My course $s")
                        val cTemp = s.split(" ")
                        val name = cTemp[0].trim()
                        val sec = cTemp[1].trim()
                        val course = courseR.findCourse(name, sec)
                        if (course != null){
                            courseList.add(course)
//                        println("My course object ${course._id}")
                        }
                    }
                    setMyCourseList(courseList.toList())
                }


            }
        }
    }

    fun getFriendCourses(id: String, setCourseList: (String) -> Unit){
        viewModelScope.launch {
            val friendCourses = fpr.getFriendCourses(id)
            setCourseList(friendCourses)
        }
    }

    fun friendsAndCourses(
        setLoading: (Boolean) -> Unit,
        setData: (MutableMap<String, MutableMap<String, String>>) -> Unit,
        setEmpty: (Boolean) -> Unit,
        addNonEmpty: (String) -> Unit
    ){
        viewModelScope.launch {
            var finalMap = mutableMapOf<String, MutableMap<String, String>>().apply {
                days.forEach { day ->
                    this[day] = mutableMapOf<String, String>().apply {
                        com.aaditx23.bracusocial.ui.screens.Routine.timeSlots.forEach { time -> this[time] = "" }
                    }
                }
            } // {Day: {Time: Name-Course-Sec-Room} }
            val map = fpr.getAllFriendCourses() // Name: "Courses"
//            println("MAP $map")
            if (map.isEmpty()){
                delay(200)
                setEmpty(true)
                setLoading(false)
            }
            else{
                setEmpty(false)
                val dataMap = mutableMapOf<String, List<String>>()  // Name: ["CourseData"]
                for((name, courseString) in map){
                    val temp = courseString.split(",")
                    val tempCourseList = mutableListOf<String>()
                    temp.forEach { course ->
                        val (courseName, section) = course.trim().split(" ")
                        val courseData = courseR.findCourse(courseName, section)
                        if (courseData!= null){
                            val tempStringList = courseR.courseToString(courseData)
                            tempStringList.forEach { string ->
                                if(string.isNotEmpty()){
                                    tempCourseList.add(string)
                                }
                            }
                            dataMap[name] = tempCourseList.toList()
                        }
                    }
                }   // dataMap populated
//                println("DATAMAP $dataMap")
                for ((name, course) in dataMap){
                    course.forEach { c->
                        val temp = c.split(",") // ["name.section.time.day.room"]
                        temp.forEach { courseString ->
                            val courseData = courseString.split(".")    // [name,section,time,day,room]
//                            println("Temp $courseData")
                            val day = courseData[3].split(" ")
                            val time = courseData[2]
                            day.forEach { d->
                                val value = "$name.$c"
                                val key = d.trim()
                                val timeValue = finalMap[key]?.get(time)

                                if (timeValue == null) {
                                    finalMap[key]?.set(time, value)
                                } else {
                                    println("PREV $timeValue")
                                    finalMap[key]?.set(time, "$timeValue|$value")
                                }

                                addNonEmpty(d.trim())
//                                println("$d: $time: $name.$c")
                            }
                        }

                    }
                }   // // {Day: {Time: Name.coursename.section.time.day.room} } populated
                delay(1000)
                setLoading(false)
                setData(finalMap)
            }

        }
    }


}