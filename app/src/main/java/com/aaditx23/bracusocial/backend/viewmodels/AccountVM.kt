package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.ProfileProxyRepository
import com.aaditx23.bracusocial.component6
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

//    val firstProfile = profileR.getFirstProfile()



    fun createProfile(
        profileData: List<String>,
        ifRepeat: (Boolean) -> Unit
    ) {

        viewModelScope.launch {
            val (id,password,name,courses,addedFriends,friendRequests) = profileData
            val remoteProfile = ppR.getProfileProxy(id)
            if (remoteProfile == null) {
                profileR.createProfile(
                    sid = id,
                    name = name,
                    pass = password,
                    courses = courses,
                    friends = addedFriends,
                    requests = friendRequests
                )
                sessionR.loginStatusUpdate(true)
                ppR.createProfile(
                    sid = id,
                    name = name,
                    pass = password,
                    courses = courses,
                    friends = addedFriends,
                    requests = friendRequests
                )
            }
            else{
                ifRepeat(false)
            }
        }
    }

    suspend fun createFriends(){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if(me != null){
                println("PROFILE FOUND")
                me.addedFriends.split(",").forEachIndexed { _, s ->
                    println("FRIEND NEEDED $s")
                    if(s!=""){
                        val friend = async {
                            ppR.getProfileProxy(s.trim())
                        }.await()
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
            else{
                println("PROFILE EMPTY")
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
            val me = profileR.getMyProfile()
           if (me != null){
               profileR.updateCourses(me.studentId, courses)
               ppR.updateCourses(me.studentId, courses)
           }
        }
    }
}



