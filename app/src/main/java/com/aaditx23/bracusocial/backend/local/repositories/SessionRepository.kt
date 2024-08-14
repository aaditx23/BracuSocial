package com.aaditx23.bracusocial.backend.local.repositories

import com.aaditx23.bracusocial.backend.local.models.Session
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createSession(){
        realm.write {
            val sessionData = Session().apply {
                dbStatus = false
                loginStatus = false
            }
            copyToRealm(sessionData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun dbStatusUpdate(status: Boolean){
        realm.write {
            val sessionData = query<Session>().first().find()
            if (sessionData != null) {
                sessionData.dbStatus = status
            }
        }
        println("DB status updated to $status")
    }


    suspend fun loginStatusUpdate(status: Boolean){
        realm.write {
            val sessionData = query<Session>().first().find()
            if (sessionData != null) {
                sessionData.loginStatus = status
            }
        }
        println("Login status updated to $status")
    }
    suspend fun deleteSession(id: ObjectId){
        realm.write {
            val sessionObject = query<Session>("_id == $0", id).first().find()
            if (sessionObject != null) {
                delete(sessionObject)
            }
        }
    }

    fun getAllSession() : Flow<List<Session>> {
        return realm
            .query<Session>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }


}