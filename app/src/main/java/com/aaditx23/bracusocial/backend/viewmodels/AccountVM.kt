package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import com.aaditx23.bracusocial.backend.remote.ProfileProxyRepository
import com.aaditx23.bracusocial.component6
import com.aaditx23.bracusocial.component7
import com.aaditx23.bracusocial.component8
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
    private val ppR: ProfileProxyRepository,
    private val courseR: CourseRepository
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

    ) {

        viewModelScope.launch {
            val (id,password,name,courses,addedFriends,friendRequests, profilePic, email) = profileData
//            val remoteProfile = ppR.getProfileProxy(email)
//            if (remoteProfile == null) {
            profileR.createProfile(
                sid = id,
                name = name,
                pass = password,
                courses = courses,
                friends = addedFriends,
                requests = friendRequests,
                pic = profilePic,
                emailData = email
            )
            sessionR.loginStatusUpdate(true)
            ppR.createProfile(
                sid = id,
                name = name,
                pass = password,
                courses = courses,
                friends = addedFriends,
                requests = friendRequests,
                pic = profilePic,
                emailData = email
            )
//            }
//            else{
//                ifRepeat(false)
//            }
        }
    }

    suspend fun createFriends(){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if(me != null){
//                println("PROFILE FOUND")
                me.addedFriends.split(",").forEachIndexed { _, s ->
//                    println("FRIEND NEEDED $s")
                    if(s!=""){
                        val friend = async {
                            ppR.getProfileProxy(s.trim())
                        }.await()
                        if (friend != null) {
                            fpR.createFriendProfile(
                                sid = friend.studentId,
                                name = friend.studentName,
                                courses = friend.enrolledCourses,
                                friends = friend.addedFriends,
                                pic = friend.profilePicture,
                                emailData = friend.email
                            )
                        }
                    }
                }
            }
            else{
//                println("PROFILE EMPTY")
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
            val me = async{ profileR.getMyProfile() }.await()
           if (me != null){
               profileR.updateCourses(courses)
               ppR.updateCourses(me.studentId, courses)
           }
        }
    }

    fun getMyCourses(setCourses: (MutableList<Course>) -> Unit){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if (me != null){
                val courseStringList = me.enrolledCourses.split(",")
                val courseList = mutableListOf<Course>()
                courseStringList.forEachIndexed { _, s ->
                    if (s != ""){
//                        println("$s is the course, accountvm getmuycourses")
                        val (name, seciton) =  s.split(" ")
                        val course = courseR.findCourse(name.trim(), seciton.trim())
                        if (course != null){
                            courseList.add(course)
                        }
                    }
                }
                setCourses(courseList)
            }
        }
    }

    suspend fun updateName(name: String){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if (me != null){
                profileR.updateName(name = name)
                ppR.updateName(me.studentId, name = name)
            }
        }
    }
    suspend fun updatePic(pic: String){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if (me != null){
                profileR.updatePic(pic = pic)
                ppR.updatePic(me.studentId, pic = pic)
            }
        }
    }
}





