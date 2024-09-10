package com.aaditx23.bracusocial.backend.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.FriendProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.ProfileRepository
import com.aaditx23.bracusocial.backend.local.repositories.SavedRoutineRepository
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import com.aaditx23.bracusocial.backend.remote.ProfileProxyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class FriendsVM @Inject constructor(
    private val profileR: ProfileRepository,
    private val fpR: FriendProfileRepository,
    private val ppR : ProfileProxyRepository
): ViewModel() {

    val allFriendProfiles = fpR.getAllFriendProfile()
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
//    val firstProfile = profileR.getMyProfile()

    fun addFriend(friend: String){
        viewModelScope.launch {
             // add friend id to string
            // fetch profile from remote
            val remoteProfile = ppR.getProfileProxy(friend)
            if (remoteProfile != null){
                profileR.addFriend(friend)
                fpR.createFriendProfile(
                    sid = remoteProfile.studentId,
                    name = remoteProfile.studentName,
                    courses = remoteProfile.enrolledCourses,
                    friends = remoteProfile.addedFriends,
                    pic = remoteProfile.profilePicture,
                    emailData = remoteProfile.email
                )
                val me = profileR.getMyProfile()
                ppR.updateFriend(me!!.studentId, friend)
                // add me to friend's profile

            }
            else{
//                println("No such friend $friend in server")
            }
        }
    }

    fun unfriend(friend: String){
        viewModelScope.launch {
            profileR.removeFriend(friend)   //remove friend from profile
            fpR.deleteFriendProfile(friend) // remove friend from local
            val me = profileR.getMyProfile()
            ppR.updateFriend(me!!.studentId, friend, add = false)
        }
    }

    fun sendRequest(friend: String){
        viewModelScope.launch {
            val me = profileR.getMyProfile()
            ppR.sendFriendRequest(
                from = me!!.studentId,
                to = friend
            )
        }
    }

    fun isInRequest(friend: String, result: (Boolean) -> Unit){
        viewModelScope.launch {
            val friendProfile = async{ ppR.getProfileProxy(friend) }.await()
            val me = async{ profileR.getMyProfile() }.await()

//            println("FOUND FRIEND ${friendProfile!!.studentId}")
            if (friendProfile != null ){
                if(friendProfile.friendRequests.contains(me!!.studentId)){
//                    println("${ friendProfile.friendRequests } here")
                    result(true)
                }
                else{
                    result(false)
                }
            }
        }
    }

    fun getFriendProfile(friend: String): ProfileProxy?{
        return ppR.getProfileProxy(friend)

    }
    fun cancelRequest(friend: String){
        viewModelScope.launch {
            val meLocal = profileR.getMyProfile()
            if(meLocal != null){
                ppR.cancelRequest(friend, meLocal.studentId)
                profileR.cancelRequest(friend)

            }

        }
    }

    fun cancelSentRequest(friend: String){
        viewModelScope.launch {
            val meLocal = profileR.getMyProfile()
            if (meLocal != null){
                ppR.cancelRequest( meLocal.studentId, friend)
            }
        }
    }



}