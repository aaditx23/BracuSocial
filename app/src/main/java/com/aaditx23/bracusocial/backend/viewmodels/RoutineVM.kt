package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                val courseList = mutableListOf<Course>()
                temp.forEach { s ->
                    println("My course $s")
                    val cTemp = s.split(" ")
                    val name = cTemp[0].trim()
                    val sec = cTemp[1].trim()
                    val course = courseR.findCourse(name, sec)
                    if (course != null){
                        courseList.add(course)
                        println("My course object ${course._id}")
                    }
                }
                setMyCourseList(courseList.toList())
            }
        }
    }

    fun getFriendCourses(id: String, setCourseList: (String) -> Unit){
        viewModelScope.launch {
            val friendCourses = fpr.getFriendCourses(id)
            setCourseList(friendCourses)
        }
    }


}