package com.tutorial.bracusocial.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.tutorial.bracusocial.data.entities.CourseData
import com.tutorial.bracusocial.data.entities.Misc
import com.tutorial.bracusocial.data.entities.User

@Dao
interface Dao {
    //User
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from User where id = :id")
    suspend fun getCurrentUser(id: Int): User?

    //CourseData
    @Upsert
    suspend fun upsertCourse(course: CourseData)

    @Delete
    suspend fun deleteCourse(course: CourseData)

    @Query("select * from CourseData where courseName = :courseName and section = :section")
    fun getCourseDetails(courseName: String, section: Int): CourseData?

    @Query("SELECT * FROM CourseData ORDER BY courseName ASC, section ASC")
    fun getCourseList(): List<CourseData>

    @Query("select * from CourseData where courseName = :name and section = :section")
    fun getCourseByKey(name: String, section: String): CourseData?

    //Misc
    @Upsert
    suspend fun upsertMisc(course: Misc)

    @Delete
    suspend fun deleteMisc(course: Misc)

    @Query("select * from Misc where login = :flag")
    fun getMiscInfo(flag: Boolean): Misc?
}