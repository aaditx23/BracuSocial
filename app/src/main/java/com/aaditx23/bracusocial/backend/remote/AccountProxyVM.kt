package com.aaditx23.bracusocial.backend.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AccountProxyVM @Inject constructor(
    private val ppR: ProfileProxyRepository,
    private val sessionR: SessionRepository,
    private val profileR: ProfileRepository
): ViewModel() {

    val allSessions = sessionR.getAllSession()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val allProfiles = ppR.getAllProfileProxy()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


    fun createProfile(profileData: List<String>) {
        viewModelScope.launch {
            ppR.createProfile(
                sid = profileData[0],
                name = profileData[1],
                pass = profileData[2]
            )
        }
    }

    fun getProfile(sid: String): ProfileProxy? {
        return ppR.getProfileProxy(sid)
    }

    fun deleteLocalProfile(sid: String) {
        viewModelScope.launch {
            ppR.deleteProfileProxy(sid)
            sessionR.loginStatusUpdate(false)
        }
    }

    fun deleteAllProfiles() {
        viewModelScope.launch {
            ppR.deleteAllProfileProxy()
            sessionR.loginStatusUpdate(false)
        }
    }

    fun login(sid: String, pass: String, result: (Boolean, Boolean) -> Unit){
        viewModelScope.launch {
            val profile = ppR.getProfileProxy(sid)
            if (profile != null){
                result(pass == profile.password, true)
                if(pass == profile.password){
                    sessionR.loginStatusUpdate(true)
                    profileR.createProfile(
                        sid = profile.studentId,
                        name = profile.studentName,
                        pass = profile.password,
                        friends = profile.addedFriends,
                        courses = profile.enrolledCourses,
                        requests = profile.friendRequests
                    )
                }

            }
            else{
                result(false, false)
            }
        }
    }

}



