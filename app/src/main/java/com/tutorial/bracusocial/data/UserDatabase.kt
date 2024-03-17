package com.tutorial.bracusocial.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorial.bracusocial.data.entities.CourseData
import com.tutorial.bracusocial.data.entities.Misc
import com.tutorial.bracusocial.data.entities.User
import com.tutorial.bracusocial.data.entities.Friends

@Database(
    entities = [
        User :: class,
        CourseData:: class,
        Misc:: class,
        Friends:: class
               ],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class UserDatabase:RoomDatabase() {
    abstract val dao: Dao
    companion object {
        private var instance: UserDatabase? = null

        // Create or retrieve an instance of the database
        fun getInstance(context: Context): UserDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_data.db"
                ).build()
                instance = database
                database
            }
        }
    }
}