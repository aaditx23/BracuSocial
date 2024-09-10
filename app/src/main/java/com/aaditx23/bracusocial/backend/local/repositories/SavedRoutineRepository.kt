package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.SavedRoutine
import com.aaditx23.bracusocial.backend.local.models.Session
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class SavedRoutineRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createSavedRoutine(courseKey: String){
        realm.write {
            val savedCourseData = SavedRoutine().apply{
                courseKeyList = courseKey

            }
            copyToRealm(savedCourseData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    fun getAllSavedRoutines() : Flow<List<SavedRoutine>> {
        return realm
            .query<SavedRoutine>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }

    suspend fun deleteSavedRoutine(id: ObjectId){
//        println("deleteing routine from repi")
        realm.write {
            val savedRoutine = query<SavedRoutine>("_id == $0", id).first().find()
            if (savedRoutine != null){
//                println("Found course")
                delete(savedRoutine)
            }
        }
    }

    suspend fun deleteAllSavedRoutine(){
        realm.write {
            val savedRoutines = query<SavedRoutine>().find()
            delete(savedRoutines)
        }
    }

}