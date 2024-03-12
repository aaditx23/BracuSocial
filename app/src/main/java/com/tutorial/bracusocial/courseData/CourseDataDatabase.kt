package com.tutorial.bracusocial.courseData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CourseData::class],
    version = 1
)

abstract class CourseDataDatabase: RoomDatabase(){

    abstract val dao:CourseDataDao

    companion object {
        private var instance: CourseDataDatabase? = null

        // Create or retrieve an instance of the database
        fun getInstance(context: Context): CourseDataDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    CourseDataDatabase::class.java,
                    "course_data.db"
                ).build()
                instance = database
                database
            }
        }
    }

}