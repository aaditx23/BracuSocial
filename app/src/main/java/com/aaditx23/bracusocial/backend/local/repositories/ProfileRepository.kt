package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createProfile(
        sid: String,
        name: String,
        pass: String,
        courses: String,
        friends: String,
        requests: String,
        pic: String,
        emailData: String

    ){
        realm.write {
            val profileData = Profile().apply {
                studentId = sid
                studentName = name
                password = pass
                enrolledCourses = courses
                addedFriends = friends
                friendRequests = requests
                profilePicture = pic
                email = emailData
            }
            copyToRealm(profileData, updatePolicy = UpdatePolicy.ALL)
            println("Profile created (profile repository, create profile)")
        }
    }

    suspend fun updateName(name: String){
        realm.write {
            val profileData = query<Profile>().first().find()
            if (profileData != null) {
                profileData.studentName = name
            }
        }
        println("Name updated to $name")
    }
    suspend fun updatePic(pic: String){
        realm.write {
            val profileData = query<Profile>().first().find()
            if (profileData != null) {
                profileData.profilePicture = pic
            }
        }
        println("Name updated to $pic")
    }
    suspend fun updateCourses(courses: String){
        realm.write {
            val profileData = query<Profile>().first().find()
            if (profileData != null) {
                profileData.enrolledCourses = courses
            }
        }
        println("Name updated to $courses")
    }

    suspend fun addFriend(friend: String){
        realm.write {
            val profileData = query<Profile>().first().find()
            if (profileData != null) {
                if(profileData.addedFriends == ""){
                    profileData.addedFriends = friend
                }
                else{
                    if(!profileData.addedFriends.contains(friend)) {
                        profileData.addedFriends = "${profileData.addedFriends},$friend"
                    }
                }
                val temp = stringToSet(profileData.friendRequests)
                var tempReq: String =""
                if (temp.isNotEmpty()){
                    temp.remove(friend.toInt())
                    temp.forEachIndexed { i, s ->
                        tempReq = if (i==0){
                            s.toString()
                        } else{
                            "$tempReq,$s"
                        }

                    }
                }
                profileData.friendRequests = tempReq

            }
        }
        println("Friend added $friend")
    }
    suspend fun removeFriend(friend: String){
        realm.write {
            val profileData = query<Profile>().first().find()
            if (profileData != null) {
//                profileData.addedFriends  = "${profileData.addedFriends},$friend"
                val temp = stringToSet(profileData.addedFriends)
                var tempFriend: String =""
                if (temp.isNotEmpty()){
                    temp.remove(friend.toInt())
                    temp.forEachIndexed { i, s ->
                        tempFriend = if (i == 0){
                            s.toString()
                        } else{
                            "$tempFriend,$s"
                        }

                    }
                }
                profileData.addedFriends = tempFriend

            }
        }
        println("Friend removed $friend")
    }
    private fun makeFriendRequest(profileData: ProfileProxy, friend: String){
        if(profileData.friendRequests == ""){
            profileData.friendRequests = friend
        }
        else{
            if(!profileData.friendRequests.contains(friend)) {
                profileData.friendRequests = "${profileData.friendRequests},$friend"
            }
        }
    }

    suspend fun sendFriendRequest(from: String, to: String){
        realm.write {
            val toProfile = query<ProfileProxy>("studentId == $0", to).first().find()
            if(toProfile != null){
                makeFriendRequest(toProfile, from)
            }
        }
    }
    suspend fun cancelRequest(friend: String){
        realm.write {
            val profileData = query<Profile>().first().find()

            if(profileData!= null ){
                removeFriendRequest(profileData, friend)
            }
        }
    }

    private fun removeFriendRequest(profileData: Profile, friend: String){
        if (profileData.friendRequests != "" && profileData.friendRequests.contains(friend)){
            println("FRIEND IS HERE $friend ${profileData.friendRequests}")
            val temp = profileData.friendRequests.split(",")
            var tempReq = ""
            temp.forEachIndexed { _, s ->
                if(s != friend && s!= "") tempReq = "$tempReq,$s"
            }
            profileData.friendRequests = tempReq
        }
    }


    suspend fun deleteProfile(sid: String){
        realm.write {
            val profileObject = query<Profile>("studentId == $0", sid).first().find()
            if (profileObject != null) {
                delete(profileObject)
            }
        }
    }
    suspend fun deleteAllProfile(){
        realm.write {
            val profiles = query<Profile>().find()
            delete(profiles)
        }
    }

    fun getAllProfile() : Flow<List<Profile>> {
        return realm
            .query<Profile>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }
    fun getMyProfile(): Profile?{
        return realm.query<Profile>().first().find()
    }

    private fun stringToSet(st: String): MutableSet<Int>{
        val t = st.split(",")
        val temp = mutableSetOf<Int>()
        t.forEach{ string ->
            if(string != "") temp.add(string.toInt())
        }
        return temp
    }
}