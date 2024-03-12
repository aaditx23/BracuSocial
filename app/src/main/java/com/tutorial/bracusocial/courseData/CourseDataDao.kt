package com.tutorial.bracusocial.courseData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CourseDataDao {

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


}