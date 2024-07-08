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
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.mongodb.kbson.ObjectId
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

}