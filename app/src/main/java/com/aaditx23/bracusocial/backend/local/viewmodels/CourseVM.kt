package com.aaditx23.bracusocial.backend.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class CourseVM @Inject constructor(
    private val courseR: CourseRepository
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

}