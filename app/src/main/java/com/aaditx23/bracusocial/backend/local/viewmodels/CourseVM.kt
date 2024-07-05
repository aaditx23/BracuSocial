package com.aaditx23.bracusocial.backend.local.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class CourseVM @Inject constructor(
    private val courseR: CourseRepository,
    private val uc: UsisCrawler
    ): ViewModel() {

    val allCourses = courseR.getAllCourses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun createCourseDB(
        courseName: String,
        section: String,
        classDay: String,
        classTime: String,
        classRoom: String,
        labDay: String,
        labTime: String,
        labRoom: String

        ){
        viewModelScope.launch {
            courseR.createCourse(
                listOf(
                    courseName,
                    section,
                    classDay,
                    classTime,
                    classRoom,
                    labDay,
                    labTime,
                    labRoom
                )
            )
        }
    }

    fun deleteCourse(id: ObjectId){
        viewModelScope.launch {
            courseR.deleteCourse(id)
        }
    }

    fun populateDb(){
        var allCoursesJson = mutableListOf<JSONObject>()
        viewModelScope.launch {
            allCoursesJson = uc.executeAsyncTask()
            allCoursesJson.forEachIndexed { _, course ->
                val lab = course.getBoolean("Lab")
                val courseInfo: List<String>
                if (lab){
                    courseInfo = listOf(
                        course.optString("Course"),
                        course.optString("Section"),
                        course.getJSONArray("ClassDay").join(" - "),
                        course.optString("ClassTime"),
                        course.optString("ClassRoom"),
                        course.getJSONArray("LabDay").join(" - "),
                        course.optString("LabTime"),
                        course.optString("LabRoom")
                    )
                }
                else{
                    courseInfo = listOf(
                        course.optString("Course"),
                        course.optString("Section"),
                        course.getJSONArray("ClassDay").join(" - "),
                        course.optString("ClassTime"),
                        course.optString("ClassRoom"),
                        "-",
                        "-",
                        "-"
                    )
                }
                courseR.createCourse(courseInfo)
                println("Course created $courseInfo")
            }
        }
    }

    fun clearDB(){
        val db = allCourses.value
        viewModelScope.launch {
            db.forEachIndexed { _, course ->
                courseR.deleteCourse(course._id)
            }
        }
    }

}