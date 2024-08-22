package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.ProfileProxyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AccountVM @Inject constructor(
    private val profileR: ProfileRepository,
    private val sessionR: SessionRepository,
    private val fpR: FriendProfileRepository,
    private val ppR: ProfileProxyRepository
): ViewModel() {

    val allSessions = sessionR.getAllSession()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val allProfiles = profileR.getAllProfile()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val firstProfile = profileR.getFirstProfile()



    fun createProfile(
        profileData: List<String>,
        ifRepeat: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val remoteProfile = ppR.getProfileProxy(profileData[0])
            if (remoteProfile == null) {
                profileR.createProfile(
                    sid = profileData[0],
                    name = profileData[1],
                    pass = profileData[2],
                    courses = profileData[3]
                )
                sessionR.loginStatusUpdate(true)
                ppR.createProfile(
                    sid = profileData[0],
                    name = profileData[1],
                    pass = profileData[2],
                    courses = profileData[3]
                )
            }
            else{
                ifRepeat(false)
            }
        }
    }

    fun createFriends(){
        viewModelScope.launch {
            val me = profileR.getFirstProfile()
            if(me != null){
                me.addedFriends.split(",").forEachIndexed { _, s ->
                    println("$me \n found \n $s")
                    if(s!=""){
                        val friend = ppR.getProfileProxy(s)
                        if (friend != null) {
                            fpR.createFriendProfile(
                                sid = friend.studentId,
                                name = friend.studentName,
                                courses = friend.enrolledCourses,
                                friends = friend.addedFriends
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteLocalProfile(sid: String) {
        viewModelScope.launch {
            profileR.deleteProfile(sid)
            sessionR.loginStatusUpdate(false)
        }
    }

    fun deleteAllProfiles() {
        viewModelScope.launch {
            profileR.deleteAllProfile()
            fpR.deleteAllFriendProfile()
            sessionR.loginStatusUpdate(false)
        }
    }
    fun clearFriends(){
        viewModelScope.launch {
            fpR.deleteAllFriendProfile()
        }
    }

    fun addCourses(courses: String){
        viewModelScope.launch {
            val me = profileR.getFirstProfile()
           if (me != null){
               profileR.updateCourses(me.studentId, courses)
               ppR.updateCourses(me.studentId, courses)
           }
        }
    }
}



