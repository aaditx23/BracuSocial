package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.local.models.Profile
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendProfileRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createFriendProfile(
        sid: String,
        name: String,
        courses: String,
        friends: String

    ){
        realm.write {
            val profileData = FriendProfile().apply {
                studentId = sid
                studentName = name
                enrolledCourses = courses
                addedFriends = friends
            }
            copyToRealm(profileData, updatePolicy = UpdatePolicy.ALL)
            println("Created friend profile $sid fprR, createFP")
        }
    }


    fun friendList(): Flow<String>{
        val profile = realm.query<Profile>().first().find()
        val list = profile!!.addedFriends.split(",")
        return list.asFlow()
    }



    suspend fun deleteFriendProfile(sid: String){
        realm.write {
            val profileObject = query<FriendProfile>("studentId == $0", sid).first().find()
            if (profileObject != null) {
                delete(profileObject)
            }
        }
    }
    suspend fun deleteAllFriendProfile(){
        realm.write {
            val profiles = query<FriendProfile>().find()
            delete(profiles)
        }
    }

    fun getAllFriendProfile() : Flow<List<FriendProfile>> {
        return realm
            .query<FriendProfile>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }
    fun getAllFriendCourses(): Map<String, String> {
        return realm
            .query<FriendProfile>()
            .find()
            .associate { it.studentName to it.enrolledCourses }
    }
    suspend fun getFriendCourses(id: String): String {
        val friendProfile = realm
            .query<FriendProfile>("studentId == ?", id)
            .find()
            .firstOrNull()

        return friendProfile?.enrolledCourses ?: ""
    }




}