package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.local.models.Session
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createProfile(
        sid: String,
        name: String,
        courses: MutableList<Course>
    ){
        realm.write {
            val profileData = Profile().apply {
                studentId = sid
                studentName = name
                enrolledCourses = courses as RealmList<Course>
            }
            copyToRealm(profileData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun updateName(sid: String, name: String){
        realm.write {
            val profileData = query<Profile>("studentId == $0", sid).first().find()
            if (profileData != null) {
                profileData.studentName = name
            }
        }
        println("Name updated to $name")
    }
    suspend fun updateCourseList(sid: String, courses: MutableList<Course>){
        realm.write {
            val profileData = query<Profile>("studentId == $0", sid).first().find()
            if (profileData != null) {
                val prevCourses = profileData.enrolledCourses
                profileData.enrolledCourses = (prevCourses + courses)
                    .sortedBy { it.courseName }
                    .toMutableList() as RealmList<Course>
            }
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

    fun getAllProfile() : Flow<List<Profile>> {
        return realm
            .query<Profile>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }


}