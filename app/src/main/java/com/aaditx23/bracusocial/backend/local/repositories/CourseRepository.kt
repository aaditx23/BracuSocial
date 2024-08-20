package com.aaditx23.bracusocial.backend.local.repositories

import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.Session
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class CourseRepository @Inject constructor(
    private val realm: Realm
) {


    suspend fun createCourse(course: List<String>){
        realm.write {
            val courseData = Course().apply{
                courseName= course[0]
                section= course[1]
                classDay = course[2]
                classTime = course[3]
                classRoom = course[4]
                labDay = course[5]
                labTime = course[6]
                labRoom = course[7]

            }
            copyToRealm(courseData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    fun getAllCourses() : Flow<List<Course>> {
        return realm
            .query<Course>()
            .asFlow()
            .map { results ->
                results.list.toList()
            }
    }

    fun getAllClass(): Flow<List<String?>> {
        return realm
            .query<Course>()
            .asFlow()
            .map { results ->
                val data = results.list.map { "${it.classRoom}" }.toSet().sortedBy { it ?: "" }
                data
            }
    }
    fun getAllLab(): Flow<List<String>> {
        return realm
            .query<Course>()
            .asFlow()
            .map { course ->
                course.list
                    .filter { it.labRoom != null && it.labRoom != "-" && !it.labRoom!!.startsWith("FT") }
                    .map { it.labRoom!! }
                    .toSet()
                    .sorted()
            }
    }

    fun findOccupiedClass(time: String, day: String): List<String>{
        println("Time: $time day : $day")
        return realm
            .query<Course>(
                "classTime == $0 AND classDay CONTAINS $1", time, day
            )
            .find()
            .map { it.classRoom.toString() }
            .toSet()
            .toList()
    }
    fun findOccupiedLab(time: String, day: String): List<String>{
        println("Time: $time day : $day")
        return realm
            .query<Course>(
                "labTime == $0 AND labDay CONTAINS $1", time.trim(), day.trim()
            )
            .find()
            .map { it.labRoom.toString() }
            .toSet()
            .toList()
    }

    suspend fun deleteCourse(id: ObjectId){
        println("deleteing course from repi")
        realm.write {
            val courseObject = query<Course>("_id == $0", id).first().find()
            if (courseObject != null){
                println("Found course")
                delete(courseObject)
            }
        }
    }

    suspend fun deleteAllCourses(){
        realm.write {
            val courses = query<Course>().find()
            delete(courses)
        }
    }

    suspend fun findCourse(name: String, section: String): Course?{
        return realm
            .query<Course>(
                "courseName == $0 AND section == $1", name, section
            ).first().find()

    }

    suspend fun courseToString(course: Course): List<String>{
        val cs = "${course.courseName}.${course.section}"
        var cls = "$cs.${course.classTime}.${course.classDay}.${course.classRoom}"
        var lab =
            if(course.labRoom != "-") "$cs.${course.labTime}.${course.labDay}.${course.labRoom}"
            else ""
        // coursename.section.time.day.room

        return listOf(
            cls,
            lab
        )
    }

}