package com.aaditx23.bracusocial.backend.remote

import com.aaditx23.bracusocial.backend.local.models.Profile
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileProxyRepository @Inject constructor(
    private val realm: Realm,
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
            val profileData = ProfileProxy().apply {
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
//            println("Profile created (profile repository, create profile)")
        }
    }

    suspend fun updateName(sid: String, name: String){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", sid).first().find()
            if (profileData != null) {
                profileData.studentName = name
            }
        }
//        println("Name updated to $name")
    }
    suspend fun updatePic(sid: String, pic: String){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", sid).first().find()
            if (profileData != null) {
                profileData.profilePicture = pic
            }
        }
//        println("Name updated to $pic")
    }
    suspend fun updateCourses(sid: String, courses: String){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", sid).first().find()
            if (profileData != null) {
                profileData.enrolledCourses = courses
            }
        }
//        println("Name updated to $courses")
    }

    suspend fun addFriend(me: String, friend: String){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", me).first().find()
            val friendData = query<ProfileProxy>("studentId == $0", friend).first().find()
            if (profileData != null && friendData != null) {
                addFriendToList(profileData, friend)
                addFriendToList(friendData, profileData.studentId)
            }
        }
//        println("Friend added $friend")
    }
    suspend fun updateFriend(me: String, friend: String, add:Boolean = true){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", me).first().find()
            val friendData = query<ProfileProxy>("studentId == $0", friend).first().find()
            if (profileData != null && friendData != null) {
                if(add){
                    addFriendToList(profileData, friend)
                    addFriendToList(friendData, profileData.studentId)
//                    println("Friend added $friend")
                }
                else{
                    removeFriendFromList(profileData, friend)
                    removeFriendFromList(friendData, profileData.studentId)
//                    println("Friend removed $friend")
                }
            }
        }

    }
    private fun addFriendToList(profileData: ProfileProxy, friend: String){

        if(!profileData.addedFriends.contains(friend)) {
            profileData.addedFriends = "${profileData.addedFriends},$friend"
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


    private fun removeFriendFromList(profileData: ProfileProxy, friend: String){
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

    private fun makeFriendRequest(profileData: ProfileProxy, friend: String){


        if(!profileData.friendRequests.contains(friend)) {
            profileData.friendRequests = "${profileData.friendRequests},$friend"
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

    suspend fun cancelRequest(from: String, to: String){
        realm.write {
            val profileData = query<ProfileProxy>("studentId == $0", to).first().find()

            if(profileData!= null ){
                removeFriendRequest(profileData, from)
            }
        }
    }

    private fun removeFriendRequest(profileData: ProfileProxy, friend: String){
        if (profileData.friendRequests != "" && profileData.friendRequests.contains(friend)){
//            println("FRIEND IS HERE in remote$friend ${profileData.friendRequests}")
            val temp = profileData.friendRequests.split(",")
            var tempReq = ""
            temp.forEachIndexed { _, s ->
                if(s != friend && s!= "") tempReq = "$tempReq,$s"
            }
            profileData.friendRequests = tempReq
        }
    }

    suspend fun cancelFriendRequest(friend: String){

    }



    suspend fun deleteProfileProxy(sid: String){
        realm.write {
            val profileObject = query<ProfileProxy>("studentId == $0", sid).first().find()
            if (profileObject != null) {
                delete(profileObject)
            }
        }
    }
    suspend fun deleteAllProfileProxy(){
        realm.write {
            val profiles = query<ProfileProxy>().find()
            delete(profiles)
        }
    }

    fun getAllProfileProxy() : Flow<List<ProfileProxy>> {
        return realm
            .query<ProfileProxy>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }
    fun getProfileProxy(email: String) : ProfileProxy? {
        return realm
            .query<ProfileProxy>("email == $0", email).first().find()

    }
    fun getProfileProxyId(id: String) : ProfileProxy? {
        return realm
            .query<ProfileProxy>("studentId == $0", id).first().find()

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