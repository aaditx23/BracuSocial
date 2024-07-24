package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.SavedRoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class SavedRoutineVM @Inject constructor(
    private val srR: SavedRoutineRepository,
    private val courseR: CourseRepository
): ViewModel() {

    val allSavedRoutines = srR.getAllSavedRoutines()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


    fun addSavedRoutine(courseKey: String){
        viewModelScope.launch {
            srR.createSavedRoutine(courseKey)
        }
    }

    fun removeSavedRoutine(id: ObjectId){
        viewModelScope.launch {
            srR.deleteSavedRoutine(id)
        }
    }

    fun clearAll(){
        viewModelScope.launch {
            srR.deleteAllSavedRoutine()
        }
    }



}