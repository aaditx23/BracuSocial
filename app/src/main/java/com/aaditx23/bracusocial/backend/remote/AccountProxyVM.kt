package com.aaditx23.bracusocial.backend.remote

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.component6
import com.aaditx23.bracusocial.component7
import com.aaditx23.bracusocial.component8
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AccountProxyVM @Inject constructor(
    private val ppR: ProfileProxyRepository,
    private val sessionR: SessionRepository,
    private val profileR: ProfileRepository,
    private val fpR: FriendProfileRepository,
    private val usisClient: USISClient,
    private val fbp: FirebaseRepository,
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
        val (id,password,name,courses,addedFriends,friendRequests, profilePic, email) = profileData
        viewModelScope.launch {
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
        }
    }

    fun getProfile(email: String): ProfileProxy? {
        return ppR.getProfileProxy(email)
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

    suspend fun login(
        email: String,
        pass: String,
        result: (login: Boolean, name: String, gotProfile: Boolean) -> Unit
    ){
        viewModelScope.launch {
            val (login, name) = async{ usisClient.loginAndFetchName(email, pass) }.await()


            if(login){
                println("In vm login is true")
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
                            println("Friend is $s")
                            val friend = async {
                                ppR.getProfileProxyId(s)
                            }.await()
                            if (friend != null) {
                                println("Friend found $s , accountproxyvm, login")
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

}



