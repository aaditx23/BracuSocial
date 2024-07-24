package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class SessionVM @Inject constructor(
    private val sessionR: SessionRepository
    ): ViewModel() {

    val allSessions = sessionR.getAllSession()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val firstSession: Flow<Session?> = allSessions.map{ sessions ->
        sessions.firstOrNull()
    }


    fun createSession(){
        viewModelScope.launch {
            sessionR.createSession()
        }
    }

    fun deleteSession(id: ObjectId){
        viewModelScope.launch {
            sessionR.deleteSession(id)
        }
    }

    fun updateSessionDbStatus(status: Boolean){
        viewModelScope.launch {
            sessionR.dbStatusUpdate(status)
        }
    }


}