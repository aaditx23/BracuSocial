package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.FirebaseRepository
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import com.aaditx23.bracusocial.backend.remote.ProfileProxyRepository
import com.aaditx23.bracusocial.backend.remote.RemoteProfile
import com.aaditx23.bracusocial.backend.remote.USISClient
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
    private val courseR: CourseRepository,
    private val fbp: FirebaseRepository,
    private val usisClient: USISClient
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


    suspend fun login(
        email: String,
        pass: String,
        result: (login: Boolean, name: String, gotProfile: Boolean) -> Unit
    ){
        viewModelScope.launch {
            val (login, name) = async{ usisClient.loginAndFetchName(email, pass) }.await()


            if(login){
//                println("In vm login is true")
                val profile =  async {
                    fbp.getProfileByEmail(email)
                }.await()

                if (profile != null){
                    profileR.createProfile(
                        sid = profile.studentId,
                        name = name,
                        pass = pass,
                        friends = profile.addedFriends,
                        courses = profile.enrolledCourses,
                        requests = profile.friendRequests,
                        pic = profile.profilePicture,
                        emailData = profile.email
                    )

                    profile.addedFriends.split(",").forEachIndexed{_, s ->
                        if(s != ""){
//                            println("Friend is $s")
                            val friend = async {
                                ppR.getProfileProxyId(s)
                            }.await()
                            if (friend != null) {
//                                println("Friend found $s , accountproxyvm, login")
                                fpR.createFriendProfile(
                                    sid = s,
                                    name = friend.studentName,
                                    courses = friend.enrolledCourses,
                                    friends = friend.addedFriends,
                                    pic = friend.profilePicture,
                                    emailData = friend.email
                                )
                            }
                        }

                    }
                    sessionR.loginStatusUpdate(true)

                }
                result(true, name, profile != null)

            }
            else{
                result(false, "No name", false)
            }


        }
    }

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
            fbp.addOrUpdateProfile(
                RemoteProfile(
                    studentId = id,
                    name = name,
                    email = email,
                    addedFriends = "",
                    enrolledCourses = "",
                    friendRequests = "",
                    profilePicture = profilePic
                )
            )
            sessionR.loginStatusUpdate(true)
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
               fbp.updateEnrolledCoursesByEmail(me.email, courses)
           }
        }
    }

    suspend fun updateProfileFromRemote(){
        viewModelScope.launch {
//            println("Updating profile")
            val me = async{ profileR.getMyProfile() }.await()
            if (me != null){
                val remoteMe = async { fbp.getProfileByEmail(me.email) }.await()
                if(remoteMe!= null){
                    profileR.updateProfile(remoteMe)
                }
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
    suspend fun updatePic(pic: String, result: (Boolean) -> Unit){
        viewModelScope.launch {
            val me = async{ profileR.getMyProfile() }.await()
            if (me != null){
                profileR.updatePic(pic = pic, result = result)
                fbp.updatePictureByEmail(me.email, pic)
            }
        }
    }
}





